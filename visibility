
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
     $id = $_GET["id"];
     //$date = $_GET["date"];
    //$time = $_GET["time"];
    
    
        $sql = "SELECT  * FROM `Pi_Ads`  WHERE `Id`='$id'";
        if(mysqli_num_rows($conn->query($sql)) == 0)
        {
             echo"No ads found for this location" ;
        }
        
        else
        {
             $sql = "UPDATE `Pi_Ads` SET `Visibility` = `Visibility` + 1 WHERE `id`='$id'";
             $res = $conn->query($sql);
             $results = array();
	        foreach($res as $r){
			    array_push($results, $r);
	        }
		echo "Visibility counter incremented";
     }
     
    $conn->close();
}
   
?>
