<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */

include '../../lib/database.php';
include '../../lib/layout.php';

$sid = mysql_escape_string($_GET['sid']);

$db_service = $SQL->getOne("SELECT * FROM service WHERE id='$sid'");

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
<div id="subhead">Service - Details<br />
<b><? print ($db_service['name']); ?></b></div>
<br />
<table cellspacing="0" class="contenttable">
	<tr class="tabtitle">
		<td colspan="3">Servicekonfiguration</td>
	</tr>
	<tr class="tabtitle">
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr class="lightgrey">
		<td>&nbsp;</td>
		<td><a class="menu" href="servicemake.php?sid=<? print($sid); ?>">Marken/Modelle
		Editieren</a></td>
		<td>&nbsp;</td>
	</tr>
	<tr class="darkgrey">
		<td>&nbsp;</td>
		<td><a class="menu" href="serviceoption.php?sid=<? print($sid); ?>">Optionen/Werte
		Editieren</a></td>
		<td>&nbsp;</td>
	</tr>
	<tr class="lightgrey">
		<td>&nbsp;</td>
		<td><a class="menu" href="servicevalidate.php?sid=<? print($sid); ?>">Konfiguration
		validieren</a></td>
		<td>&nbsp;</td>
	</tr>
	<tr class="darkgrey">
		<td>&nbsp;</td>
		<td><a class="menu" href="do_service.php?method=export&amp;sid=<? print($sid); ?>">Konfiguration exportieren</a></td>
		<td>&nbsp;</td>
	</tr>
	<tr class="darkgrey">
		<td colspan="3">&nbsp;</td>
	</tr>
</table>
<br />
<form action="do_service.php" method="post">
<table cellspacing="0" class="contenttable">
	<tr class="tabtitle">
		<td colspan="4">Service</td>
	</tr>
	<tr class="tabtitle">
		<td colspan="4">&nbsp;</td>
	</tr>
	<tr class="lightgrey">
		<td>&nbsp;</td>
		<td>Prefix</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr class="lightgrey">
		<td>&nbsp;</td>
		<td colspan="2"><input type="text" name="prefix"
			value="<? print($db_service['prefix']); ?>" size="135" /></td>
		<td>&nbsp;</td>
	</tr>
	<tr class="darkgrey">
		<td>&nbsp;</td>
		<td>Period</td>
		<td><input type="text" name="period"
			value="<? print($db_service['period']); ?>" /></td>
		<td>&nbsp;</td>
	</tr>
	<tr class="lightgrey">
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td><input type="submit" value="&Auml;ndern" /></td>
		<td>&nbsp;</td>
	</tr>
	<tr class="darkgrey">
		<td>&nbsp;</td>
		<td><input type="hidden" name="method" value="edit" /></td>
		<td><input type="hidden" name="sid" value="<?print $sid; ?>" /></td>
		<td>&nbsp;</td>
	</tr>
</table>
</form>
<br/>
<form action="do_service.php" method="post">
<table cellspacing="0" class="contenttable">
	<tr class="tabtitle">
		<td colspan="4">Service Löschen</td>
	</tr>
	<tr class="tabtitle">
		<td colspan="4">&nbsp;</td>
	</tr>
	<tr class="lightgrey">
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td><input onclick="return confirm('Sicher?');" type="submit" value="L&ouml;schen" /></td>
		<td>&nbsp;</td>
	</tr>
	<tr class="darkgrey">
		<td>&nbsp;</td>
		<td><input type="hidden" name="method" value="delete" /></td>
		<td><input type="hidden" name="sid" value="<?print $sid; ?>" /></td>
		<td>&nbsp;</td>
	</tr>
</table>
</form>

</div>
<div id="footer">(c) 2010</div>
</div>
</body>
</html>
