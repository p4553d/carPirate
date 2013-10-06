<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */

if(!empty($_POST['sent'])){
	$name = $_POST['name'];
	$email = $_POST['email'];
	$message = $_POST['message'];

	include 'lib/email.php';

	if(!empty($name) && validEmail($email) && !empty($message)){
		$message = "----\nName:".$name."\nEmail:".$email."\n-----\n".$message;
		$sent = mail("p4553d@googlemail.com", "Message from CarPirate.de", $message);
	}

}
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
	content="CarPirate - Kostenloses Autosuchrogramm für jedermann. Kontaktform für Feedback." />
<link type="text/css" href="/main.css" rel="stylesheet" media="screen" />
<title>CarPirate - Autosuchprogramm - Kontakt</title>
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
<br/>
<a class="addthis_button" href="http://www.addthis.com/bookmark.php?v=250&amp;username=xa-4b8996ef0f0b26a2"><img src="http://s7.addthis.com/static/btn/v2/lg-share-en.gif" width="125" height="16" alt="Bookmark and Share" style="border:0"/></a><script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#username=xa-4b8996ef0f0b26a2"></script>
</div>
<div id="content">
<div id="subhead">CarPirate - Kontakt</div>
<div id="breadcrumb">&gt;&nbsp;<a class="menu" href="/">Start</a>&gt;&nbsp;<a
	class="menu" href="/contact.php">Kontakt</a></div>
<p>Deine Meinung ist sehr wichtig für uns! Falls Du Anregungen oder
Kritik loswerden willst, nutze das unten stehende Formular um eine
Nachricht zu hinterlassen. Natürlich helden wir Dir auch gerne und
beantworten Deine Fragen.</p>

<? if(!$sent): ?>
<form action="" method="post">
<table style="width: 100%; vertical-align: top;">
	<tr>
		<td><b>Name:</b></td>
		<td><input type="text" name="name" size="50"
			value="<? print $name; ?>" /></td>
	</tr>
	<tr>
		<td><b>E-Mail:</b></td>
		<td><input type="text" name="email" size="50"
			value="<? print $email; ?>" /></td>
	</tr>
	<tr>
		<td><b>Nachricht:</b></td>
		<td><textarea name="message" cols="45" rows="15"><? print $message; ?></textarea></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="submit" name="sent" value="Absenden!" /></td>
	</tr>
</table>
</form>
<? else: ?>
<p>Deine Nachricht wurde versendet!</p>
<? endif; ?></div>
<div id="footer">(c) 2010</div>
</div>
<?php include 'analytics.php'; ?>
</body>
</html>
