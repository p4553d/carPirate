<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */

// build gui.in.xml

include_once ("../lib/database.php");

function updateGUI(){
	GLOBAL $SQL;
	$xml_gui = simplexml_load_file("../download/gui.in.xml");

	//get MakeModel information
	$db_makemodel = $SQL->getAll("SELECT
					mm.name as make,
					ml.name as model
				FROM
					master_make mm,
					master_model ml
				WHERE
					mm.id = ml.makeid ORDER BY make,model");

	$xml_mmdata = $xml_gui->makemodeldata;
	foreach ($db_makemodel as $makemodel) {
		$xml_make = $xml_mmdata->xpath("make[@key='".htmlspecialchars($makemodel['make'])."']");
		if(empty($xml_make)){
			$xml_make = $xml_mmdata->addChild('make', '');
			$xml_make->addAttribute('key', htmlspecialchars($makemodel['make']));
		}

		$xml_model = $xml_make[0]->xpath("model[@key='".htmlspecialchars($makemodel['make'])."']");
		if(empty($xml_model)){
			$xml_model = $xml_make[0]->addChild('model', '');
			$xml_model->addAttribute('key', htmlspecialchars($makemodel['model']));
		}
	}

	//get Option information
	$db_makeoption = $SQL->getAll("SELECT
					mo.name as name,
					mv.value as value
					FROM
					master_option mo,
					master_optionvalue mv
					WHERE
					mo.id = mv.optionid ORDER BY name,LENGTH(value),value");


	$xml_options = $xml_gui->options;
	foreach ($db_makeoption as $option) {
		$xml_opt = $xml_options->$option['name'];
		if(empty($xml_opt)){
			$xml_opt = $xml_options->addChild($option['name']);
		}

		$xml_val = $xml_opt->xpath("option[@key='".htmlspecialchars($option['value'])."']");
		if(empty($xml_val)){
			$xml_val = $xml_opt->addChild('option','');
			$xml_val->addAttribute('key', htmlspecialchars($option['value']));
		}
	}

//	header("content-type: application/xml");
//	print $xml_gui->asXML();

	$zip = new ZipArchive;
	if ($zip->open("../download/crawler_original.jar") === TRUE) {
		$zip->addFromString('gui/gui.in.xml', $xml_gui->asXML());
		$zip->close();
	}
	else{
		print("AAAAA!");
		exit();
	}
}
?>