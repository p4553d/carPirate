<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */

include '../../lib/database.php';
include '../../lib/layout.php';

$sid = mysql_escape_string($_GET['sid']);

$db_service = $SQL->getOne("SELECT * FROM service WHERE id='$sid'");

$db_make = $SQL->getAll("SELECT
id, CONCAT('<a class=\"menu\" href=\"mastermodel.php?makeid=',id,'\">', name, '</a>'),
CONCAT('<a class=\"menu\" href=\"do_servicemake.php?method=copy&amp;sid=$sid&amp;mid=',master_make.id,'\">&gt;&gt;</a>')
FROM
master_make
WHERE
master_make.id NOT IN (
SELECT service_make.masterid FROM service_make WHERE service_make.serviceid = '$sid')");

$db_model = $SQL->getAll("SELECT
master_model.id as id,
master_make.name as make,
CONCAT('<a class=\"menu\" href=\"mastermodel.php?makeid=',master_make.id,'\">', master_model.name, '</a>'),
CONCAT('<a class=\"menu\" href=\"do_servicemake.php?method=copy&amp;sid=$sid&amp;mid=',master_make.id,'\">&gt;&gt;</a>')
FROM
master_model, master_make
WHERE
master_model.id NOT IN (
SELECT service_model.masterid FROM service_model WHERE service_model.serviceid = '$sid')
AND
master_model.makeid = master_make.id
ORDER BY make");

$db_option = $SQL->getAll("SELECT
id, name,
CONCAT('<a class=\"menu\" href=\"do_serviceoption.php?method=copy&amp;sid=$sid&amp;mid=',master_option.id,'\">&gt;&gt;</a>')FROM
master_option
WHERE
master_option.id NOT IN (
SELECT service_option.masterid FROM service_option WHERE service_option.serviceid = '$sid')");

$db_optionvalue = $SQL->getAll("SELECT
val.id as id,
opt.name as opt,
CONCAT('<a class=\"menu\" href=\"masteroptionvalue.php?optionid=',opt.id,'\">', val.value, '</a>') as value,
CONCAT('<a class=\"menu\" href=\"do_serviceoption.php?method=copy&amp;sid=$sid&amp;mid=',opt.id,'\">&gt;&gt;</a>')
FROM
master_optionvalue val, master_option opt
WHERE
val.id NOT IN (
SELECT service_optionvalue.masterid FROM service_optionvalue WHERE service_optionvalue.serviceid = '$sid')
AND
val.optionid = opt.id
ORDER BY opt");

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link type="text/css" href="/main.css" rel="stylesheet" media="screen" />
<title>Service - Details - <? print ($db_service['name']); ?></title>
</head>
<body>
<div id="page">
<div id="header">Header</div>
<div id="left">
<ul>
	<li>Master
	<ul>
		<li><a class="menu" href="/admin/master/mastermake.php">Marken/Modelle</a></li>
		<li><a class="menu" href="/admin/master/masteroption.php">Optionen/Werte</a></li>
		<li><a class="menu" href="/admin/master/do_mastermake.php">jar-Update</a></li>
	</ul>
	</li>
	<li><a class="menu" href="/admin/service/service.php">Service</a>
	<ul>
		<li><a class="menu" href="/admin/service/servicedetail.php?sid=<?print $sid; ?>">Details</a></li>
		<li><a class="menu" href="/admin/service/servicemake.php?sid=<?print $sid; ?>">Marken/Modelle</a></li>
		<li><a class="menu" href="/admin/service/serviceoption.php?sid=<?print $sid; ?>">Optionen/Werte</a></li>
		<li><a class="menu" href="/admin/service/servicevalidate.php?sid=<?print $sid; ?>">Validation</a></li>
	</ul>
	</li>
	<li>Log
	<ul>
		<li><a class="menu" href="/admin/log/showlog.php">Aktionen</a></li>
		<li><a class="menu" href="/admin/log/showinstance.php">Instanzen</a></li>
	</ul>
	</li>
</ul>
</div>
<div id="content">
<div id="subhead">Service - Validation<br />
<b><? print ($db_service['name']); ?></b></div>
<? print maketable("Fehlende Marken", array("id", "name", "copy"), $db_make); ?>
<br />
<? print maketable("Fehlende Modele", array("id", "make", "name", "copy"), $db_model);?>
<br />
<? print maketable("Fehlende Optionen", array("id", "name", "copy"), $db_option);?>
<br />
<? print maketable("Fehlende Optionenwerte", array("id", "option", "value", "copy"), $db_optionvalue);?>
</div>
<div id="footer">(c) 2010</div>
</div>
</body>
</html>
