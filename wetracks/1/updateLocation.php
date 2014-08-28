<?php
  //$id_user=$_GET['id_user'];
  //$latitude=$_GET['latitude'];
  //$longitude=$_GET['longitude'];
  $id_user=$_POST['id_user'];
  $latitude=$_POST['latitude'];
  $longitude=$_POST['longitude'];

  $connection = mysql_connect('localhost','root','liaops');
  mysql_select_db('wt_server',$connection);
  $sql="select id from locations where id_user='$id_user';";
  $result = mysql_query($sql) or die ("Error:" . mysql_error());
  
  if (mysql_num_rows($result) > 0){
  	$sql1 = $query = "update locations set latitude='$latitude', longitude='$longitude' where id_user='$id_user'";
  	$result1 = mysql_query($sql1) or die ("Error" . mysql_error());
  	if($result1)
  		echo "1";
  	else
  		echo "0";
  } else {
  	$sql2 = "insert into locations (id_user, latitude, longitude) values ('$id_user','$latitude','$longitude')";
  	$result2 = mysql_query($sql2) or die ("Error" . mysql_error());
  	if($result2)
  		echo "1";
  	else
  		echo "0";
  }
  

?>
