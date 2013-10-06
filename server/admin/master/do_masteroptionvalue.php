<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */


include '../../lib/database.php';
include '../../download/update.php';

$method = $_REQUEST['method'];
$optionid = $_REQUEST['optionid'];

switch ($method){
	case "new":
		$name = $_REQUEST['name'];
		$SQL->insert("master_optionvalue", array("value" => dbstring($name), "optionid" => dbstring($optionid)));
		break;

	case "delete":
		$id = mysql_escape_string($_REQUEST['id']);
		$SQL->delete("master_optionvalue", "id='$id'");
		break;
}

updateGUI();

header("location: masteroptionvalue.php?optionid=$optionid");
?>