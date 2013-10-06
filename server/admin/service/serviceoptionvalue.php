<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */

include '../../lib/database.php';
include '../../lib/layout.php';

$optid = mysql_escape_string($_GET['optid']);
$sid = mysql_escape_string($_GET['sid']);

$db_optionvalue = $SQL->getAll("SELECT
ml.id,
ml.value,
CONCAT('<input size=\"100\" type=\"text\" name=\"',ml.id,'\" value=\"',sl.value,'\" />')
FROM
service_optionvalue sl, master_optionvalue ml
WHERE
sl.masterid = ml.id AND ml.optionid = '$optid' AND sl.serviceid = '$sid'");

$db_option = $SQL->getAll("SELECT
m.id, m.name,
CONCAT('<input size=\"100\" type=\"text\" name=\"make\" value=\"',s.value,'\" />')
FROM master_option m, service_option s
WHERE
m.id=s.masterid AND
m.id='$optid' AND s.serviceid='$sid'");

$db_service = $SQL->getOne("SELECT * FROM service WHERE id='$sid'");
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link type="text/css" href="/main.css" rel="stylesheet" media="screen" />
<title>Service Modelle - Marke <? print ($db_option['name']); ?></title>
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
<div id="subhead">Service Optionwerte<br />
<b><? print ($db_service['name']); ?></b><br />
Option <b><? print ($db_option[0]['name']); ?></b></div>
<form
	action="do_serviceoption.php?<?print("method=edit&amp;sid=$sid&amp;mid=$optid"); ?>"
	method="post">
<? print(maketable("Option", array("id", "name", "value"), $db_option)); ?>
<br />
<?
$db_optionvalue[] = array("&nbsp;", "&nbsp;", "&nbsp;");
$db_optionvalue[] = array("&nbsp;", "&nbsp;", "<input type=\"submit\" />");
print(maketable("Optionwerte", array("id", "name", "value"), $db_optionvalue)); ?>
</form>
<br />
<form action="do_serviceoption.php?sid=<? print $sid; ?>" method="post">
<table cellspacing="0" class="contenttable">
	<tr class="tabtitle">
		<td colspan="4">Option Löschen</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="hidden" name="method" value="delete" /> <input
			type="hidden" name="mid" value="<? print($optid); ?>" /></td>
		<td><input onclick="return confirm('Sicher?');" type="submit"
			value="Löschen" /></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>
</table>
</form>
</div>
<div id="footer">(c) 2010</div>
</div>
</body>
</html>
