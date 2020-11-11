
<?php
    header('Access-Control-Allow-Origin: *');
	$servername = "localhost";
	$username = "u435393063_sjec";
	$password = "sjec@123";
	$db="u435393063_sjec";
	// Create connection
	$conn = new mysqli($servername, $username, $password,$db);
	// Check connection
 
if ($conn->connect_error) 
{
	$response["error"] = "Connection failed: " . $conn->connect_error;
	$response["success"] = "";
}
else
{
    //$location = $_GET["location"];
    //$date = $_GET["date"];
    //$time = $_GET["time"];
    
    
        $sql = "SELECT  * FROM `Pi_Ads`  WHERE 1";
        if(mysqli_num_rows($conn->query($sql)) == 0)
        {
             echo "No ads found for this location" ;
        }
        
        else
        {
            
             $sql = "Select * from  `Pi_Ads` WHERE 1";
             $res = $conn->query($sql);
        	$results = array();
	        foreach($res as $r){
			    array_push($results, $r);
	        }
		echo json_encode($results);
     }
    $conn->close();
}
   
?>
