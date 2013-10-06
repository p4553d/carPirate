<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */


include '../../lib/database.php';
include '../../download/update.php';

$method = $_REQUEST['method'];
$makeid = $_REQUEST['makeid'];

switch ($method){
	case "new":
		$name = $_REQUEST['name'];
		$SQL->insert("master_model", array("name" => dbstring($name), "makeid" => dbstring($makeid)));
		break;

	case "delete":
		$id = mysql_escape_string($_REQUEST['id']);
		$SQL->delete("master_model", "id='$id'");
		break;
}

updateGUI();

header("location: mastermodel.php?makeid=$makeid");
?>