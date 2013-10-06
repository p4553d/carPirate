<?php
include './lib/layout.php';
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta name="Language" content="de" />
<meta name="robots" content="index, follow" />
<meta name="keywords"
	content="autosuchprogramm, CarPirate, automarkt, autosuche, gebrauchtwagen, gebrauchtauto,  suchprogramm, freeware" />
<meta name="description"
	content="CarPirate - Kostenloses Autosuchrogramm für jedermann. Effiziente und einfache Gebrauchtwagensuche in Echtzeit." />
<link type="text/css" href="/main.css" rel="stylesheet" media="screen" />
<title>CarPirate - Autosuchprogramm - Automatische Gebrauchtwagensuche</title>
</head>
<body>
<div id="page">
<div id="header">
<div id="title"><a href="/">CarPirate.de</a></div>
</div>
<div id="left">
<ul>
	<li><a class="menu" href="/">Home</a></li>
	<li><a class="menu" href="screenshots.php">Screenshots</a></li>
	<li><a class="menu" href="download.php">Download</a></li>
	<li><a class="menu" href="documentation.php">Dokumentation</a></li>
	<li><a class="menu" href="contact.php">Kontakt</a></li>
</ul>
<br />
<a class="addthis_button"
	href="http://www.addthis.com/bookmark.php?v=250&amp;username=xa-4b8996ef0f0b26a2"><img
	src="http://s7.addthis.com/static/btn/v2/lg-share-en.gif" width="125"
	height="16" alt="Bookmark and Share" style="border: 0" /></a><script
	type="text/javascript"
	src="http://s7.addthis.com/js/250/addthis_widget.js#username=xa-4b8996ef0f0b26a2"></script>
</div>
<div id="content">
<div id="subhead">Willkommen bei CarPirate</div>
<div id="breadcrumb">&gt;&nbsp;<a class="menu" href="/">Start</a></div>

<div style="padding: 5px;">
Dieses Programm hilft Dir bei der Suche nach dem Auto Deiner Wahl. <br /><br />
Obgleich Autoscout24, PKW.de oder aber auch andere bekannte Autosuchmaschinen: <br />
die globale Suche von <b>CarPirate</b> vereinigt alle Suchergebnisse, sortiert diese <br />
nach gewünschten Kriterien (wie zum Beispiel Baujahr, Preis oder Kilometerstand),  <br />
filtert nach Marken, Modellen und Parametern und speichert interessante Sucherbegnisse <br />
als Favoriten - das Alles in Echtzeit. <br /><br />
Mit <b>CarPirate</b> sparst du wirklich Zeit und hast alle Internetangebote direkt im Überblick. <br />
Wir nutzen unsere Software selbst und möchten das Jedermann/-frau davon profitiert. <br />
Gratis und unverbindlich.
<div>


<br />
<br />

<?
$db_news[] = array("31.03.2010",
"Version 0.9.2 ist online!:<br/>
Zu den Verbesserungen zählen geänderte Suchfunktion und angepasste Benutzeroberfläche.<br/>
<a href=\"download.php\">Jetzt testen!</a>
");
$db_news[] = array("10.02.2010",
"Wir sind in einem Pre-Release Zustand:<br/>
Das Programm ist funktionsfähig und enthält alle geplannten Features.
Später werden sicherlich noch weitere dazukommen, damit die Suche nach Autos zu einer wirklich bequemen Angelegenheit werden kann.<br/>
Wir würden uns richtig freuen uns wenn DU das Programm testest und uns einen Feedback gibst!
");
$db_news[] = array("27.02.2010",
"Es können erste Erfolge und mit Hilfe des Programms verbucht werden.
Auch sind einige Testberichte angekommen, an dieser Stelle: Danke nochmals an alle Tester! Wir sind jetzt dabei, sie durchzuarbeiten.");
?> <? print(maketable("Aktuelles:", array("", ""), $db_news)); ?></div>
<div id="footer">(c) 2010</div>
</div>
<?php include 'analytics.php'; ?>
</body>
</html>

