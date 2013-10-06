<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */
include '../../lib/database.php';

header("Content-type: text/plain");

set_time_limit(600);

$combineXML = simplexml_load_file('./combine.xml', null, LIBXML_NOCDATA);

print "service\n";

$name = $combineXML->host;
print("name: $name\n");

$prefix = $combineXML->url . $combineXML->prefix;
print("prefix: $prefix\n");

$period = $combineXML->period;
print("period: $period\n");

$db_service = array(
	"name"		=> dbstring($name),
	"prefix"	=> dbstring($prefix),
	"period"	=> dbstring($period)
);

$SQL->insert("service", $db_service);
$db_service = $SQL->getOne("SELECT * FROM service WHERE name='$name'");


 print("\nmake-model\n");

 $l_make = $combineXML->search->makemodeldata->make;
 foreach ($l_make as $make) {
 print($make['key']."\n");

 $SQL->query("INSERT INTO service_make (masterid, serviceid, value)
 SELECT id, '".$db_service['id']."', '".$make['value']."' FROM master_make WHERE name='".$make['key']."'");

 $l_model = $make->model;
 foreach ($l_model as $model){
 print("-".$model['key']."\n");
 $SQL->query("
 INSERT INTO
 service_model (masterid, serviceid, value)
 SELECT
 master_model.id, '".$db_service['id']."', '".$model['value']."'
 FROM master_make, master_model
 WHERE
 master_make.name='".$make['key']."' AND
 master_model.makeid = master_make.id AND
 master_model.name='".$model['key']."'");

 }
 print("\n");
 }


print("\noptionen\n");
$l_option = $combineXML->search->option->children();
foreach ($l_option as $option) {

	print($option['key']."\n");
	$SQL->query("INSERT INTO service_option (masterid, serviceid, value)
 SELECT id, '".$db_service['id']."', '".$option['prefix']."'
 FROM master_option
 WHERE name='".$option['key']."'");


	$l_value = $option->children();
	foreach ($l_value as $value){
		print("- ".$value['key']."\n");
		$SQL->query("
		 INSERT INTO
		 service_optionvalue (masterid, serviceid, value)
		 SELECT
		 master_optionvalue.id, '".$db_service['id']."', '".$value['value']."'
		 FROM master_option, master_optionvalue
		 WHERE
		 master_option.name='".$option['key']."' AND
		 master_optionvalue.optionid = master_option.id AND
		 master_optionvalue.value='".$value['key']."'");
	}
}

print("\ninterval\n");
$l_option = $combineXML->search->interval->children();
foreach ($l_option as $option) {

	print($option['key']."\n");
	$SQL->query("INSERT INTO service_option (masterid, serviceid, value)
 SELECT id, '".$db_service['id']."', '".$option['from']."'
 FROM master_option
 WHERE name='".$option['key']."From'");
	$SQL->query("INSERT INTO service_option (masterid, serviceid, value)
 SELECT id, '".$db_service['id']."', '".$option['to']."'
 FROM master_option
 WHERE name='".$option['key']."To'");

	$l_value = $option->children();
	foreach ($l_value as $value){
		print("- ".$value['key']."\n");
		$SQL->query("
		 INSERT INTO
		 service_optionvalue (masterid, serviceid, value)
		 SELECT
		 master_optionvalue.id, '".$db_service['id']."', '".$value['value']."'
		 FROM master_option, master_optionvalue
		 WHERE
		 master_option.name='".$option['key']."From' AND
		 master_optionvalue.optionid = master_option.id AND
		 master_optionvalue.value='".$value['key']."'");
		$SQL->query("
		 INSERT INTO
		 service_optionvalue (masterid, serviceid, value)
		 SELECT
		 master_optionvalue.id, '".$db_service['id']."', '".$value['value']."'
		 FROM master_option, master_optionvalue
		 WHERE
		 master_option.name='".$option['key']."To' AND
		 master_optionvalue.optionid = master_option.id AND
		 master_optionvalue.value='".$value['key']."'");
	}
}

print("\ncheckbox\n");
$l_option = $combineXML->search->checkbox->children();
foreach ($l_option as $option) {

	print($option['key']."\n");

	$l_value = $option->children();
	foreach ($l_value as $value){
		print("- ".$value['key']."\n");
	}
}
print("done!");





?>