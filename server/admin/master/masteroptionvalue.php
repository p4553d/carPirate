<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */

include '../../lib/database.php';
include '../../lib/layout.php';

$optionid = mysql_escape_string($_GET['optionid']);

$db_optionvalue = $SQL->getAll("SELECT
master_optionvalue.id,
master_optionvalue.value,
CONCAT('<a class=\"menu\" onclick=\"return confirm(\'Sicher?\');\" href=\"do_masteroptionvalue.php?method=delete&id=',master_optionvalue.id,'&optionid=$optionid\">[x]</a>')
FROM master_optionvalue
WHERE optionid='$optionid'
ORDER BY value");
$db_option = $SQL->getOne("SELECT * FROM master_option WHERE id='$optionid'");

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link type="text/css" href="/main.css" rel="stylesheet" media="screen" />
<title>Master Optionvalue - Option <? print ($db_option['name']); ?></title>
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
		<li><a class="menu" href="/admin/master/do_masteroption.php?optionid=<? print $optionid; ?>">jar-Update</a></li>
	</ul>
	</li>
	<li><a class="menu" href="/admin/service/service.php">Service</a></li>
	<li>Log
	<ul>
		<li><a class="menu" href="/admin/log/showlog.php">Aktionen</a></li>
		<li><a class="menu" href="/admin/log/showinstance.php">Instanzen</a></li>
	</ul>
	</li>
</ul>
</div>
<div id="content">
<div id="subhead">Master Optionvalue<br />
Option <? print ($db_option['name']); ?></div>
<? print(maketable("Optionwerte", array("id", "value", "&nbsp;"), $db_optionvalue)); ?>
<br />
<form action="do_masteroptionvalue.php" method="post">
<table cellspacing="0" class="contenttable">
	<tr class="tabtitle">
		<td colspan="4">Neuer Wert</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>Name:</td>
		<td><input type="text" name="name" /></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="hidden" name="method" value="new" /> <input
			type="hidden" name="optionid" value="<? print($optionid); ?>" /></td>
		<td><input type="submit" value="anlegen" /></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>
</table>
</form>
<br />
<form action="do_masteroption.php" method="post">
<table cellspacing="0" class="contenttable">
	<tr class="tabtitle">
		<td colspan="4">Option Löschen</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="hidden" name="method" value="delete" /> <input
			type="hidden" name="id" value="<? print($optionid); ?>" /></td>
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
