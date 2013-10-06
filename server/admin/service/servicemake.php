<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */

include '../../lib/database.php';
include '../../lib/layout.php';

$sid = $_GET['sid'];

$db_make = $SQL->getAll("SELECT
id,
CONCAT('<a class=\"menu\" href=\"servicemodel.php?sid=$sid&amp;makeid=',m.id,'\">', m.name, '</a>'),
value
FROM service_make s, master_make m
WHERE
s.masterid = m.id AND
s.serviceid='$sid'
ORDER BY name");

$db_service = $SQL->getOne("SELECT * FROM service WHERE id='$sid'");
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link type="text/css" href="/main.css" rel="stylesheet" media="screen" />
<title>Service - Make - <? print ($db_service['name']); ?></title>
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
<div id="subhead">Service Marken<br />
<b><? print ($db_service['name']); ?></b></div>
<? print(maketable("Marken", array("id", "name", "value"), $db_make)); ?>
</div>
<div id="footer">(c) 2010</div>
</div>

</body>
</html>
