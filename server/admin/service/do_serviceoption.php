<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */


include '../../lib/database.php';

$method = $_REQUEST['method'];
$masterid = $_REQUEST['mid'];
$serviceid = $_REQUEST['sid'];

switch ($method){
	case "edit":

		$SQL->update("service_option", "masterid='$masterid' AND serviceid='$serviceid'", "value='".$_POST['make']."'");

		$db_model = $SQL->getAll("SELECT
		ml.id
		FROM  master_optionvalue ml, service_optionvalue sl
		WHERE
		ml.id = sl.masterid AND
		ml.optionid = '$masterid' AND
		sl.serviceid = '$serviceid'");

		foreach ($db_model as $model) {
			$modelid = $model['id'];
			$SQL->update("service_optionvalue", "masterid='$modelid' AND serviceid='$serviceid'", "value='".$_POST[$modelid]."'");
		}
		break;

	case "copy":
		$SQL->query("INSERT IGNORE INTO service_option SET masterid=".dbstring($masterid).", serviceid=".dbstring($serviceid).", value=\"\"");

		$SQL->query("INSERT IGNORE INTO service_optionvalue
		SELECT
		l.id as masterid,
		'$serviceid' as serviceid,
		'' as value
		FROM master_option m, master_optionvalue l
		WHERE
		m.id = l.optionid AND
		m.id='$masterid'");
		break;

	case "delete":
		$SQL->query("DELETE sl FROM
		master_option as mm, master_optionvalue as ml, service_optionvalue as sl
		WHERE
		mm.id='$masterid' AND mm.id=ml.optionid AND
		sl.masterid=ml.id AND sl.serviceid='$serviceid'");

		$SQL->delete("service_option", "masterid='$masterid' AND serviceid='$serviceid'");
		header("location: serviceoption.php?sid=$serviceid");
		exit();
		break;
}

header("location: serviceoptionvalue.php?sid=$serviceid&optid=$masterid");
exit();
?>