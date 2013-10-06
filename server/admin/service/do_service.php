<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */


include '../../lib/database.php';

$method = $_REQUEST['method'];

$sid = $_REQUEST['sid'];

switch ($method){
	case "new":
		$name = $_REQUEST['name'];
		$SQL->insert("service", array("name" => dbstring($name)));
		break;

	case "edit":
		$prefix = $_REQUEST['prefix'];
		$period = $_REQUEST['period'];
		$SQL->update("service", "id='$sid'", "prefix='$prefix', period='$period'");
		header("location: servicedetail.php?sid=$sid");
		exit();
		break;

	case "export":

		//building
		$db_service = $SQL->getOne("SELECT * FROM service WHERE id='$sid'");

		$x_combine = simplexml_load_string('<?xml version="1.0" encoding="UTF-8"?><combine/>');
		$x_combine->addChild('host', htmlspecialchars($db_service['name']));
		$x_combine->addChild('url', htmlspecialchars($db_service['prefix']));
		$x_combine->addChild('period', htmlspecialchars($db_service['period']));
		$x_search = $x_combine->addChild('search','');
		$x_makemodel = $x_search->addChild('makemodeldata','');

		//get MakeModel information
		$db_makemodel = $SQL->getAll("SELECT
		mm.name as make,
		sm.value as makeval,
		ml.name as model,
		sl.value as modelval
		FROM
		master_make mm,
		master_model ml,
		service_make sm,
		service_model sl
		WHERE
		mm.id = ml.makeid AND
		sm.masterid = mm.id AND sl.masterid = ml.id AND
		sm.serviceid = sl.serviceid AND sl.serviceid = '$sid'
		ORDER BY make,model");

		foreach ($db_makemodel as $makemodel) {
			$xml_make = $x_makemodel->xpath("make[@key='".$makemodel['make']."']");
			if(empty($xml_make)){
				$xml_make = $x_makemodel->addChild('make', '');
				$xml_make->addAttribute('key', htmlspecialchars($makemodel['make']));
				$xml_make->addAttribute('value', htmlspecialchars($makemodel['makeval']));
			}

			$xml_model = $xml_make[0]->xpath("model[@key='".$makemodel['make']."']");
			if(empty($xml_model)){
				$xml_model = $xml_make[0]->addChild('model', '');
				$xml_model->addAttribute('key', htmlspecialchars($makemodel['model']));
				$xml_model->addAttribute('value', htmlspecialchars($makemodel['modelval']));
			}
		}

		$x_option = $x_search->addChild('option','');

		//get Option information
		$db_makeoption = $SQL->getAll("SELECT
		mo.name as name,
		so.value as nameval,
		mv.value as value,
		sv.value as valueval
		FROM
		master_option mo,
		master_optionvalue mv,
		service_option so,
		service_optionvalue sv
		WHERE
		mo.id = mv.optionid AND
		so.masterid = mo.id AND sv.masterid=mv.id AND
		so.serviceid = sv.serviceid AND sv.serviceid = '$sid'
		ORDER BY name,value");

		foreach ($db_makeoption as $option) {
			$xml_opt = $x_option->xpath("opt[@key='".$option['name']."']");
			if(empty($xml_opt)){
				$xml_opt = $x_option->addChild('opt','');
				$xml_opt->addAttribute('key',htmlspecialchars($option['name']));
				$xml_opt->addAttribute('prefix',htmlspecialchars($option['nameval']));
			}

			$xml_val = $xml_opt[0]->xpath("option[@key='".$option['value']."']");
			if(empty($xml_val)){
				$xml_val = $xml_opt[0]->addChild('option','');
				$xml_val->addAttribute('key', htmlspecialchars($option['value']));
				$xml_val->addAttribute('value',htmlspecialchars($option['valueval']));
			}
		}

		//output
		header("content-type: application/xml");
		header('Content-Disposition: attachment; filename="'.$db_service['name'].'.xml"');
		print $x_combine->asXML();
		exit();
		break;

	case "delete":
		$sid = mysql_escape_string($sid);
		$SQL->delete("service", "id='$sid'");
		break;
}

header("location: service.php");
?>