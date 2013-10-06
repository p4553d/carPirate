<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */


include '../../lib/database.php';
include '../../download/update.php';

$method = $_REQUEST['method'];

switch ($method){
	case "new":
		$name = $_POST['name'];
		$through = !empty($_POST['through']);
		$SQL->insert("master_option", array("name" => dbstring($name), "through" => dbstring($through)));
		break;

	case "delete":
		$id = mysql_escape_string($_REQUEST['id']);
		$SQL->delete("master_option", "id='$id'");
		break;
}

updateGUI();

header("location: masteroption.php");
?>