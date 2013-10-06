<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */


include '../../lib/database.php';
include '../../download/update.php';

$method = $_REQUEST['method'];

switch ($method){
	case "new":
		$name = $_REQUEST['name'];
		$SQL->insert("master_make", array("name" => dbstring($name)));
		break;

	case "delete":
		$id = mysql_escape_string($_REQUEST['id']);
		$SQL->delete("master_make", "id='$id'");
		break;
}

updateGUI();

header("location: mastermake.php");
?>