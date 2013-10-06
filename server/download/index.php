<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */

include('../lib/database.php');

$VERSION = "0.9.2";
$LANG = array("de", "ru", "en");

//Copy
$iid = substr(md5(microtime()),0,25)."-$VERSION";
$tmpfile = md5(microtime());

$register = ($_GET['register']!='no');
$language = strtolower($_GET['lang']);
if(!in_array($language, $LANG)){
	$language = "de";
}

$succ = copy("crawler_original.jar", "tmp/$tmpfile.jar");

if(!$succ){
	header("location: ../download.php?error=1");
	exit();
}

//Configure
//main configuration
$config = simplexml_load_string('<?xml version="1.0" encoding="UTF-8"?><config/>');
$config->addAttribute("id", "$iid");
$server = $config->addChild('server', '' );
$server->addAttribute("host", "http://www.carpirate.de/generate/");

//gui configuration
//language
$gui = simplexml_load_file('gui.out.xml');
$gui->addChild('language', $language);
// TODO: further configs


// make l4j config
$l4j_config = simplexml_load_file("crawler.xml");
$l4j_config->addChild('jar', $tmpfile.".jar");
$l4j_config->addChild('outfile', $tmpfile.".exe");
$l4j_config->asXML("tmp/$tmpfile.xml");


//write to jar
$zip = new ZipArchive;
if ($zip->open("tmp/$tmpfile.jar") === TRUE) {
	$zip->addFromString('core/config/config.xml', $config->asXML());
	$zip->addFromString('gui/gui.out.xml', $gui->asXML());
	$zip->close();
}
else{
	header("location: ../download.php?error=1");
}

//build exe
exec("java -jar launch4j/launch4j.jar tmp/$tmpfile.xml", $res);
//print_r($res);

if(!is_readable("tmp/$tmpfile.exe")){
	$output="../crawler_original";
	$iid = "anonymous fallback";
}
else{
	$output = $tmpfile;
}

if($register){
	// output/registration
	$tmp_instance = array(
			"email"			=> dbstring(""),
			"ip"			=> dbstring($_SERVER['REMOTE_ADDR']),
			"iid"			=> dbstring($iid)
	);

	$SQL->insert("core_instance", $tmp_instance);
}

header('Content-type: application/exe');
header('Content-Disposition: attachment; filename="carpirat-'.$VERSION.'.exe"');
header("Content-Transfer-Encoding: binary");
header("Content-Length: ".filesize("tmp/$output.exe")."\n");
print (file_get_contents("tmp/$output.exe"));

unlink("tmp/$tmpfile.jar");
unlink("tmp/$tmpfile.xml");
unlink("tmp/$tmpfile.exe");
exit();

?>