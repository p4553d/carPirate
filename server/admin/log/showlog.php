<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */

include '../../lib/database.php';
include '../../lib/layout.php';

$ITEMONSITE = 30;

$page = $_GET['page'];

if(empty($page)){
	$page=0;
	$offset = 0;
}else{
	$offset = $ITEMONSITE*$page;
}

$db_log = $SQL->getAll("SELECT * FROM core_log ORDER BY time DESC LIMIT ".($ITEMONSITE+1)." OFFSET $offset");
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link type="text/css" href="/main.css" rel="stylesheet" media="screen" />
<title>Log</title>
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
		<li><a class="menu" href="/admin/master/do_masteroption.php">jar-Update</a></li>
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
<div id="subhead">Log</div>
<div id="breadcrumb">&gt;Start</div>
&nbsp;
<table style="width: 100%;">
	<tr>
		<td><? if($page>0): ?> <a href="?page=<?print $page-1; ?>">&lt;vorherige
		Seite</a> <?endif; ?></td>
		<td style="text-align: right;"><? if(!empty($db_log[$ITEMONSITE])): ?> <a
			href="?page=<?print $page+1; ?>">n&auml;chste Seite&nbsp;&gt;</a> <? endif; ?>
		</td>
	</tr>
</table>
<table cellspacing="0" class="contenttable" style="font-size: 9px;">
	<tr class="tabtitle">
		<td colspan="4">Log</td>
	</tr>
	<tr class="tabhead">
		<td>time</td>
		<td>ip</td>
		<td>session</td>
		<td style="width: 50%;">action</td>
	</tr>
	<?
	$i = 0;
	foreach ($db_log as $row) : ?>
	<tr
		class="<? print ((++$i %2)? "lightgrey": "darkgrey")." ".$row['level']; ?>">
		<td><? print $row['time']; ?></td>
		<td><? print $row['ip']; ?></td>
		<td><? print substr($row['sessionid'],0,16)."<br/>".substr($row['sessionid'],17); ?></td>
		<td><? print $row['action']; ?></td>
	</tr>
	<? if(!empty($row['data'])): ?>
	<tr
		class="<? print (($i %2)? "lightgrey": "darkgrey")." ".$row['level']; ?>">
		<td>&nbsp;</td>
		<td colspan="3"><? print $row['data']; ?></td>
	</tr>
	<? endif; ?>
	<? endforeach; ?>
</table>
<table style="width: 100%;">
	<tr>
		<td><? if($page>0): ?> <a href="?page=<?print $page-1; ?>">&lt;vorherige
		Seite</a> <?endif; ?></td>
		<td style="text-align: right;"><? if(!empty($db_log[$ITEMONSITE])): ?> <a
			href="?page=<?print $page+1; ?>">n&auml;chste Seite&nbsp;&gt;</a> <? endif; ?>
		</td>
	</tr>
</table>
</div>
<div id="footer">(c) 2010</div>
</div>
</body>
</html>
