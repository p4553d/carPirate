<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */

include ('./output.lib.php');

$op = $_REQUEST['op'];
$session = $_REQUEST['sid'];

switch ($op){
	case 'reg':
		$iid = $_POST['iid'];
		header("content-type: text/plain");
		$output = getSessionId($iid);
		break;

	case 'discover':
		header("content-type: text/plain");
		// check data base
		//$output = getDiscover($session);
		$output="autoscout24.de";
		break;

	case 'service':
		header("content-type: application/xml");
		// check data base
		$srid = $_REQUEST['srid'];
		$output = getService ($session, $srid);
		break;

	case 'build':
		header("content-type: text/plain");
		// check data base
		$srid = $_REQUEST['srid'];
		$filter = $_REQUEST['filter'];
		$output = buildFilter($session, $filter, $srid);
		break;
}
print $output;
?>