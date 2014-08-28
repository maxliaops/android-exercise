<?php
  //$user=$_GET['user'];
  //$password=$_GET['password'];
  //$name=$_GET['name'];
  //$email=$_GET['email'];
  $user=$_POST['user'];
  $password=$_POST['passwd'];
  $name=$_POST['name'];
  $email=$_POST['email'];

  $connection = mysql_connect('localhost','root','153478128');
  mysql_select_db('wt_server',$connection);
  $sql = "insert into users (user, passwd, name, email) values ('$user','$password', '$name', '$email')";
  
  $result = mysql_query($sql) or die ("Error:" . mysql_error());
  if($result)
		  echo "1";
   else
          echo "0";
?>
