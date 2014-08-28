<?php

  $connection = mysql_connect('localhost','root','liaops');
  mysql_select_db('wt_server',$connection);

  //$query="SELECT * FROM locations";
  $query = "SELECT `id_user`, `latitude`, `longitude` FROM locations";
  $sql_result = mysql_query($query);
  mysql_close();
  
  $rows = array();
  while ($row = mysql_fetch_array($sql_result,MYSQL_ASSOC)) {
  	$rows[] = $row;
  }
  
  $result['result']=$rows;
  echo json_encode($result);
?>
