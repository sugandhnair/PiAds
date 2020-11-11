
<?php
    header('Access-Control-Allow-Origin: *');
	$servername = "localhost";
	$username = "u435393063_sjec";
	$pw = "sjec@123";
	$db="u435393063_sjec";
	// Create connection
	
	// Check connection
	
	if ($_SERVER["REQUEST_METHOD"] == "POST") 
    {
        $id           =      test_input($_POST["id"]);
	  	$status	        =      test_input($_POST["status"]);
	  	
	  	$conn = new mysqli($servername, $username, $pw,$db);
	  	
	  	if ($conn->connect_error) 
		{
			$response["error"] = "Connection failed2: " . $conn->connect_error;
			$response["success"] = "";
		}
	
	
    	else
    	{  
    	    date_default_timezone_set("Asia/Calcutta");
		    $l_date = date("Y-m-d");
		    $l_time = date("H:i:s"); 
    	    
    	    $sql="UPDATE `Pi_Ads` SET `Status` = '$status'  WHERE `id`='$id'";
        		
    		$run_sql=mysqli_query($conn,$sql);
        
            if ($run_sql) 
            {
                 $response["success"] = " updated";
    		     $response["error"] = "";
            }
            
            else
            {
                 $response["success"] = "";
    		     $response["error"] = "Status update failed";
            }
    	}
    	$conn->close();
    	
    	echo json_encode($response);
    }
    
function test_input($data) {
  $data = trim($data);
  $data = stripslashes($data);
  $data = htmlspecialchars($data);
  return $data;
}
?>
