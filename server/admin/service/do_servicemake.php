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

		$SQL->update("service_make", "masterid='$masterid' AND serviceid='$serviceid'", "value='".$_POST['make']."'");

		$db_model = $SQL->getAll("SELECT
		ml.id
		FROM  master_model ml, service_model sl
		WHERE
		ml.id = sl.masterid AND
		ml.makeid = '$masterid' AND
		sl.serviceid = '$serviceid'");

		foreach ($db_model as $model) {
			$modelid = $model['id'];
			$SQL->update("service_model", "masterid='$modelid' AND serviceid='$serviceid'", "value='".$_POST[$modelid]."'");
		}
		break;


	case "copy":
		$SQL->query("INSERT IGNORE INTO service_make SET masterid=".dbstring($masterid).", serviceid=".dbstring($serviceid).", value=\"\"");

		$SQL->query("INSERT IGNORE INTO service_model
		SELECT
		l.id as masterid,
		'$serviceid' as serviceid,
		'' as value
		FROM master_make m, master_model l
		WHERE
		m.id = l.makeid AND
		m.id='$masterid'");
		break;

	case "delete":
		$SQL->query("DELETE sl FROM
		master_make as mm, master_model as ml, service_model as sl
		WHERE
		mm.id='$masterid' AND mm.id=ml.makeid AND
		sl.masterid=ml.id AND sl.serviceid='$serviceid'");

		$SQL->delete("service_make", "masterid='$masterid' AND serviceid='$serviceid'");

		header("location: servicemake.php?sid=$serviceid");
		exit();
		break;
}

header("location: servicemodel.php?sid=$serviceid&makeid=$masterid");
exit();
?>