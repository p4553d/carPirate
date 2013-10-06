<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */

include '../../lib/database.php';
include '../../lib/layout.php';

$makeid = mysql_escape_string($_GET['makeid']);

$db_model = $SQL->getAll("SELECT
master_model.id,
master_model.name,
CONCAT('<a class=\"menu\" onclick=\"return confirm(\'Sicher?\');\" href=\"do_mastermodel.php?method=delete&id=',master_model.id,'&makeid=$makeid\">[x]</a>')
FROM master_model
WHERE makeid='$makeid'
ORDER BY name");
$db_make = $SQL->getOne("SELECT * FROM master_make WHERE id='$makeid'");

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link type="text/css" href="/main.css" rel="stylesheet" media="screen" />
<title>Master Modelle - Marke <? print ($db_make['name']); ?></title>
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
		<li><a class="menu" href="/admin/master/do_mastermake.php?makeid=<? print $makeid; ?>">jar-Update</a></li>
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
<div id="subhead">Master Modelle<br />
Marke <b><? print ($db_make['name']); ?></b></div>
<? print(maketable("Modelle", array("id", "name", "&nbsp;"), $db_model)); ?>
<br/>
<form action="do_mastermodel.php" method="post">
<table cellspacing="0" class="contenttable">
	<tr class="tabtitle">
		<td colspan="4">Neues Modell</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>Name:</td>
		<td><input type="text" name="name" /></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>
			<input type="hidden" name="method" value="new"/>
			<input type="hidden" name="makeid" value="<? print($makeid); ?>"/>
		</td>
		<td><input type="submit" value="anlegen"/></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>
</table>
</form>
<br/>
<form action="do_mastermake.php" method="post">
<table cellspacing="0" class="contenttable">
	<tr class="tabtitle">
		<td colspan="4">Marke Löschen</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>
			<input type="hidden" name="method" value="delete"/>
			<input type="hidden" name="id" value="<? print($makeid); ?>"/>
		</td>
		<td><input onclick="return confirm('Sicher?');" type="submit" value="Löschen"/></td>
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
