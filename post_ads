<?php
	$servername = "localhost";
	$username = "u435393063_sjec";
	$password = "sjec@123";
	$db="u435393063_sjec";
	// Create connection
	
	// Check connection
	
	if ($_SERVER["REQUEST_METHOD"] == "POST") 
    {
	  	$image	   = test_input($_POST["image"]);
	  	$title   = test_input($_POST["title"]);
	  	$description	   = test_input($_POST["desc"]);
	  	$location  = test_input($_POST["location"]);
	  	$date 	    = test_input($_POST["date"]);
	  	$time	   = test_input($_POST["time"]);
	  	$timer	   = test_input($_POST["timer"]);
	  	$count	   = test_input($_POST["count"]);
	  	
	  	$conn = new mysqli($servername, $username, $password,$db);
	  	
	  	if ($conn->connect_error) 
		{
			$response["error"] = "Connection failed: " . $conn->connect_error;
			$response["success"] = "";
		}
		
		else
		{
		    
		    $sql ="SELECT `id` FROM `Pi_Ads` ORDER BY `id` ASC";
    		$res = mysqli_query($conn,$sql); 
    		$id = 0;
    		
    		while($row = mysqli_fetch_array($res))
    		{
    			$id = $row['id'];
    		}
    		
    		$id = $id+1;
    		
    		$imageName = "ad"."_".$id;
    		
    		$path = "images/$imageName.png";
    		$image_url = "http://www.thantrajna.com/Pi_Ads/$path";
    		
    		//http://www.thantrajna.com/Pi_Ads/images/ad_10.png
		    
		    
		    
		    
		    $sql = "INSERT INTO `Pi_Ads`( `Image`, `Title`, `Description`,`Location`, `Date`, `Time`,`Timer`,`Count`) VALUES ('$image_url', '$title', '$description','$location' , '$date', '$time','$timer','$count')";
        
    	
        	 $run_sql=mysqli_query($conn,$sql);
                
                if ($run_sql) 
                {
                    file_put_contents($path,base64_decode($image));
                     $response["success"] = "Ad created..";
        		    $response["error"] = "";
                     
                } 
                else{
                    $response["success"] = "";
        		    $response["error"] = "Error  creating ad".mysqli_errorno($conn);
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
