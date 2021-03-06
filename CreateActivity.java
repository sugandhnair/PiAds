package com.example.pi_ads;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_message,et_title,et_timer,et_count;
    Spinner sp_location;
    String st_location = "";
    Button bt_create, bt_date, bt_time;
    ImageView iv_gallery;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static final int SELECT_PICTURE = 1;
    String st_url, st_image;
    private String selectedImagePath;
    ArrayList<String> list_location = new ArrayList<>();

    ArrayAdapter<String> arrayAdapter;

    RequestQueue requestQueue;
    String jsonResponse = "";

    String url_post_data = "http://thantrajna.com/Pi_Ads/postads.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        requestQueue = Volley.newRequestQueue(this);


        et_message = findViewById(R.id.et_message);
        et_timer = findViewById(R.id.et_timer);

        et_count = findViewById(R.id.et_count);
        et_title = findViewById(R.id.et_title);
        sp_location = findViewById(R.id.sp_location);
        bt_date = findViewById(R.id.bt_date);
        bt_time = findViewById(R.id.bt_time);
        iv_gallery = findViewById(R.id.iv_gallery);
        bt_date.setOnClickListener((View.OnClickListener) this);
        bt_time.setOnClickListener((View.OnClickListener) this);
        bt_create = findViewById(R.id.bt_create);
        iv_gallery = (ImageView) findViewById(R.id.iv_gallery);

        arrayAdapter = new ArrayAdapter<String>(CreateActivity.this, android.R.layout.simple_spinner_item, list_location);

        list_location.add("");
        list_location.add("Vitvara");
        list_location.add("Pumpwell");
        list_location.add("KPT");
        list_location.add("KSRTC");
        list_location.add("Jyothi");

        sp_location.setAdapter(arrayAdapter);

        bt_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st_url = et_title.getText().toString()+'+'+et_message.getText().toString()+'+'+st_location+'+'+bt_date.getText().toString()+'+'+bt_time.getText().toString();
                Toast.makeText(CreateActivity.this, st_url, Toast.LENGTH_SHORT).show();
                try {
                    volleyCreateAd();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        sp_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                st_location = list_location.get(i);

                //Toast.makeText(MainActivity.this, "Selected billing slab is : "+selected_slab, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        ((ImageView) findViewById(R.id.iv_gallery)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

    }

    public void onClick(View v) {

        if (v == bt_date) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            bt_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == bt_time) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {

                    bt_time.setText(hourOfDay + ":" + minute);

                }
            }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                st_image = BitMapToString(bitmap);
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                iv_gallery.setImageURI(selectedImageUri);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        Toast.makeText(this, "" +temp.length(), Toast.LENGTH_SHORT).show();
//        System.out.println("Encoded string :"+temp);
        return temp;
    }


    public void volleyCreateAd() throws JSONException
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_post_data,
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

                params.put("image", st_image);
                params.put("title", et_title.getText().toString());
                params.put("desc", et_message.getText().toString());
                params.put("location", st_location);
                params.put("date", bt_date.getText().toString());
                params.put("time", bt_time.getText().toString());
                params.put("timer", et_timer.getText().toString());
                params.put("count", et_count.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}