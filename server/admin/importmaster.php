<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */
include '../lib/database.php';

print("blocked! remove EXIT!");
exit();

$combineXML = simplexml_load_file('../data/gui.in.xml');
print "make\n";
// make - model
$l_make = $combineXML->makemodeldata->make;
foreach ($l_make as $make) {
	//	print($make['key']."\n");
	$bd_make = array(
 "name"	=> dbstring($make['key'])
	);
	$SQL->insert("master_make", $bd_make);

	$l_model = $make->model;
	foreach ($l_model as $model){
		$SQL->query("INSERT INTO master_model (makeid, name) SELECT id, '".$model['key']."' FROM master_make WHERE name='".$make['key']."'");
		//		print("-".$model['key']."\n");
	}
	print("\n");
}

print("optionen\n");
// optionen
//$l_option = $combineXML->xpath('/gui.in/options/*');
$l_option = $combineXML->options->children();
foreach ($l_option as $option) {
	$db_option = array(
		"name"	=> dbstring($option->getName())
	);
	$SQL->insert("master_option", $db_option);

	$l_value = $option->children();
	foreach ($l_value as $value){
		$SQL->query("INSERT INTO master_optionvalue (optionid, value) SELECT id, '".$value['key']."' FROM master_option WHERE name='".$option->getName()."'");
	}
}
print("done!");




?>