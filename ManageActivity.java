package com.example.pi_ads;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageActivity extends AppCompatActivity {

    String base_url = "http://thantrajna.com/Pi_Ads/get_info.php";
    String status_change_baseurl = "http://thantrajna.com/Pi_Ads/status_change.php?";
    String state;
    String row;
    String status_change;

    RecyclerView recyclerView;
    ArrayList<String> id = new ArrayList<>();
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> image = new ArrayList<>();
    ArrayList<String> description = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> location = new ArrayList<>();
    ArrayList<String> timer = new ArrayList<>();
    ArrayList<String> count = new ArrayList<>();
    ArrayList<String> status = new ArrayList<>();
    ArrayList<String> visibility = new ArrayList<>();
    MyAdapter myAdapter;

    RequestQueue requestQueue;
    String jsonResponse = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        recyclerView = findViewById(R.id.recyclerView);

        @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        requestQueue = Volley.newRequestQueue(this);
        try {
            volleyGetData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void volleyGetData() throws JSONException {
        id.clear();
        title.clear();
        image.clear();
        description.clear();
        timer.clear();
        count.clear();
        date.clear();
        location.clear();
        status.clear();
        visibility.clear();

        JsonArrayRequest req = new JsonArrayRequest(base_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        try {
                            jsonResponse = "";
                            int i = 0;
                            for (i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                id.add(jsonObject.getString("Id"));
                                image.add(jsonObject.getString("Image"));
                                title.add(jsonObject.getString("Title"));
                                description.add(jsonObject.getString("Description"));
                                location.add(jsonObject.getString("Location"));
                                date.add(jsonObject.getString("Date"));
                                timer.add(jsonObject.getString("Timer"));
                                count.add(jsonObject.getString("Count"));
                                status.add(jsonObject.getString("Status"));
                                visibility.add(jsonObject.getString("Visibility"));
                            }

                            myAdapter = new MyAdapter();
                            recyclerView.setAdapter(myAdapter);
                            Toast.makeText(ManageActivity.this, "" + id.size(), Toast.LENGTH_SHORT).show();



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(req);
    }

    public void volleysetstatus(final String tid, final String tstate) throws JSONException
    {
        status_change = status_change_baseurl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, status_change,
                new Response.Listener<String>()
                {
                    JSONObject res = null;
                    @Override
                    public void onResponse(String ServerResponse)
                    {
                        try {
                            res = new JSONObject(ServerResponse);

                            if(res.getString("error").length() == 0)
                            {
                                Toast.makeText(getApplicationContext(), "Thank you: "+res.getString("success"), Toast.LENGTH_LONG).show();

                                volleyGetData();
                            }

                            else
                            {
                                Toast.makeText(getApplicationContext(), "Sorry!! : "+res.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {
                            Toast.makeText(getApplicationContext(), "Error JSON : ", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        Toast.makeText(getApplicationContext(), "ERROR_2 "+volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", tid);
                params.put("status", tstate);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


//    public void volleysetstatus(String tid,String tstate) throws JSONException
//    {
//        status_change = status_change_baseurl+"id="+tid+"&status="+tstate;
//        Toast.makeText(this, status_change, Toast.LENGTH_SHORT).show();
//
//        JsonArrayRequest req = new JsonArrayRequest(status_change,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray jsonArray) {
//
//                        try {
//                            volleyGetData();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        Toast.makeText(ManageActivity.this, "api"+status_change, Toast.LENGTH_SHORT).show();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }



    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        //        private final View.OnClickListener mOnClickListener = new MyOnClickListener();
        //create inner class
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_title, tv_desc, tv_date, tv_location, tv_id, tv_timer, tv_count,tv_visibility;
            public ImageView iv_image;
            LinearLayout ll_parent;
            public Switch sw_status;

            public MyViewHolder(View itemView) {
                super(itemView);
                tv_id = itemView.findViewById(R.id.tv_id);
                tv_title = itemView.findViewById(R.id.tv_title);
                tv_desc = itemView.findViewById(R.id.tv_desc);
                tv_date = itemView.findViewById(R.id.tv_date);
                tv_location = itemView.findViewById(R.id.tv_location);
                tv_timer = itemView.findViewById(R.id.tv_timer);
                tv_count = itemView.findViewById(R.id.tv_count);
                iv_image = itemView.findViewById(R.id.iv_image);
                sw_status = itemView.findViewById(R.id.sw_status);
                tv_visibility = itemView.findViewById(R.id.tv_visibility);
                ll_parent = itemView.findViewById(R.id.ll_parent);
            }

        }


        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //load row.xml
            //pass row.xml to view holder
            View v = getLayoutInflater().inflate(R.layout.managelayout, parent, false);
            //false says that donot load at this position, loading will take care by layout manager

            MyViewHolder myViewHolder = new MyViewHolder(v);


            //return view holder
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position) // this will be called fromlayout manager
        {
            //get data based on position
            //fill data into view holder views
            byte[] decodedString = Base64.decode(image.get(position), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.tv_title.setText(title.get(position));
            holder.iv_image.setImageBitmap(decodedByte);
            holder.tv_desc.setText(description.get(position));
            holder.tv_date.setText(date.get(position));
            holder.tv_timer.setText(timer.get(position));
            holder.tv_count.setText(count.get(position));
            holder.tv_id.setText(id.get(position));
            holder.tv_location.setText(location.get(position));
            if(status.get(position).equals("1"))
            {
                holder.sw_status.setText("Enabled");
                holder.sw_status.setChecked(true);
                holder.ll_parent.setBackgroundColor(Color.parseColor("#D7ECC22C"));
            }
            else
            {
                holder.sw_status.setChecked(false);
                holder.sw_status.setText("Disabled");
                holder.ll_parent.setBackgroundColor(Color.parseColor("#BFBFC1"));
            }


            holder.tv_visibility.setText(visibility.get(position));
            holder.sw_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(b)
                        state = "1";

                    else
                        state = "0";
                    row = id.get(position);
                    try {
                        volleysetstatus(row,state);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
            });


            Glide
                    .with(ManageActivity.this)
                    .load(image.get(position))
                    .placeholder(R.drawable.load)
                    .into(holder.iv_image);


        }


        @Override
        public int getItemCount() {
            return id.size();
        }


    }

}

