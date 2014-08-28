<?php
  //$user=$_GET['user'];
  //$password=$_GET['passwd'];
  $user=$_POST['user'];
  $password=$_POST['passwd'];
  
  //echo $user;
  //echo "\n";
  //echo $password;
  //echo "\n";
  $connection = mysql_connect('localhost','root','153478128');
  mysql_select_db('wt_server',$connection);
  $sql="select * from users where user='$user';";
  $result = mysql_query($sql) or die ("Error:" . mysql_error());
  
    if (mysql_num_rows($result) > 0){
        $sql2="select id from users where user='$user' and passwd ='$password';";
        $result2 = mysql_query($sql2) or die ("Error:" . mysql_error());
        //echo $result2; 
        //echo mysql_num_rows($result2);
        if (mysql_num_rows($result2) > 0){
            //echo mysql_fetch_array($result2);
            $row = mysql_fetch_array($result2, MYSQL_NUM);
            echo $row[0];
            //printf("ID: %s", $row[0]);
        } else {
            echo -1;
        }
    } else {
        echo 0;
    }
?>