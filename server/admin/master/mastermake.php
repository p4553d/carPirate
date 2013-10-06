<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */

include '../../lib/database.php';
include '../../lib/layout.php';

$db_make = $SQL->getAll("SELECT
make.id,
CONCAT('<a class=\"menu\" href=\"mastermodel.php?makeid=',make.id,'\">', make.name, '</a>'),
count(*) as cnt
FROM master_make make LEFT JOIN master_model model
ON model.makeid = make.id
GROUP BY make.name
ORDER BY make.name");
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link type="text/css" href="/main.css" rel="stylesheet" media="screen" />
<title>Master - Make</title>
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
<div id="subhead">Master Marken</div>
<? print(maketable("Marken", array("id", "name", "Anzahl der Modelle"), $db_make)); ?>
<br />
<form action="do_mastermake.php" method="post">
<table cellspacing="0" class="contenttable">
	<tr class="tabtitle">
		<td colspan="4">Neue Marke</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>Name:</td>
		<td><input type="text" name="name" /></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="hidden" name="method" value="new"/></td>
		<td><input type="submit" value="anlegen"/></td>
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
