<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */

include '../../lib/database.php';
include '../../lib/layout.php';

$sid = $_GET['sid'];

$db_option = $SQL->getAll("SELECT
id,
CONCAT('<a class=\"menu\" href=\"serviceoptionvalue.php?sid=$sid&amp;optid=',m.id,'\">', m.name, '</a>'),
value
FROM service_option s, master_option m
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
		<li><a class="menu" href="mastermake.php">Marken/Modelle</a></li>
		<li><a class="menu" href="masteroption.php">Optionen/Werte</a></li>
		<li><a class="menu" href="do_mastermake.php">jar-Update</a></li>
	</ul>
	</li>
	<li><a class="menu" href="service.php">Service</a>
	<ul>
		<li><a class="menu" href="servicedetail.php?sid=<?print $sid; ?>">Details</a></li>
		<li><a class="menu" href="servicemake.php?sid=<?print $sid; ?>">Marken/Modelle</a></li>
		<li><a class="menu" href="serviceoption.php?sid=<?print $sid; ?>">Optionen/Werte</a></li>
		<li><a class="menu" href="servicevalidate.php?sid=<?print $sid; ?>">Validation</a></li>
	</ul>
	</li>
	<li>Log
	<ul>
		<li><a class="menu" href="showlog.php">Aktionen</a></li>
		<li><a class="menu" href="showinstance.php">Instanzen</a></li>
	</ul>
	</li>
</ul>
</div>
<div id="content">
<div id="subhead">Service Optionen<br />
<b><? print ($db_service['name']); ?></b></div>
<? print(maketable("Optionen", array("id", "name", "value"), $db_option)); ?>
</div>
<div id="footer">(c) 2010</div>
</div>

</body>
</html>
