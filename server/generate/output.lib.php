<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */

include ("../lib/database.php");

function writelog($session, $action, $data='NULL', $level='info'){
	GLOBAL $SQL;
	$tmp = array(
		"ip"		=> dbstring($_SERVER['REMOTE_ADDR']),
		"sessionid"	=> dbstring($session),
		"action"	=> dbstring($action),
		"data"		=> dbstring($data),
		"level"		=> dbstring($level)
	);
	$SQL->insert("core_log", $tmp);
}

function getSessionId($iid){
	$session = md5(time());
	//	return "ER: Validation time expiried!";

	writelog($session, "Accord session to iid=".$iid, $iid);
	return "ID:".$session;
}

function getDiscover($session){
	GLOBAL $SQL;
	//	writelog($session, "Discover services");
	$db_service = $SQL->getOne("SELECT GROUP_CONCAT(name SEPARATOR ':') as output FROM service");

	return $db_service['output'];
}

function getService($session, $srid){

	//	writelog($session, "Request service for ".$srid);
	$resXML = file_get_contents('../data/'.$srid.'/service.xml');
	return $resXML;
}

function parseFilter($filter){
	$res_filter = array();

	$res = preg_match_all('/([^=,\{\}]*)=([^=,\{}]*)/', $filter, $res_filter);

	$a_key = $res_filter[1];
	$a_value = $res_filter [2];

	$ret = array();
	foreach ($a_key as $kid => $key) {
		$ret[trim($key)] = trim($a_value[$kid]);
	}

	return $ret;

}

function buildFilter($session, $filter, $serviceid){
	GLOBAL $SQL;
	writelog($session, "Build query for $serviceid",$filter);

	$f = (parseFilter($filter));

	$result = "";

	//prefix
	$serviceid = mysql_escape_string($serviceid);
	$db_service = $SQL->getOne("SELECT * FROM service WHERE name='$serviceid'");
	$result .= $db_service['prefix'];
	$r_sid = $db_service['id'];

	//make
	$db_make = $SQL->getOne("SELECT sm.* FROM master_make mm, service_make sm WHERE mm.id=sm.masterid AND mm.name='".mysql_escape_string($f['make'])."' AND sm.serviceid='$r_sid'");
	$result .=$db_make['value'];
	if(empty($db_make['value'])){
		writelog($session,"Empty make result [$serviceid] for ".$f['make'], $filter, 'error');
		return '';
	}
	$r_mid = $db_make['masterid'];

	//model
	$db_make = $SQL->getOne("SELECT sm.value
	FROM master_model mm, service_model sm
	WHERE mm.id=sm.masterid AND
	mm.makeid='$r_mid' AND
	mm.name='".mysql_escape_string($f['model'])."' AND sm.serviceid='$r_sid'");
	$result .="&".$db_make['value'];
	if(empty($db_make['value'])){
		writelog($session,"Empty model result [$serviceid] for ".$f['model'], $filter, 'error');
		return '';
	}

	unset($f['make']);
	unset($f['model']);

	foreach ($f as $f_key => $f_val) {
		$e_key = mysql_escape_string($f_key);
		$e_val = mysql_escape_string($f_val);
		$db_option = $SQL->getOne("(SELECT
			so.value as var,
			sv.value as value
			FROM
			master_option mo, master_optionvalue mv,
			service_option so, service_optionvalue sv
			WHERE
			mo.id=so.masterid AND so.serviceid='$r_sid' AND
			mv.id=sv.masterid AND mv.optionid = mo.id AND sv.serviceid='$r_sid' AND
			mo.name='$e_key' AND mv.value='$e_val')
		UNION
		(SELECT
				so.value as var,
				'$e_val' as value
				FROM
				master_option mo,
				service_option so
				WHERE
				mo.id=so.masterid AND so.serviceid='$r_sid' AND
				mo.through = '1' AND
				mo.name='$e_key'
		)");

		$var = $db_option['var'];
		$value = $db_option['value'];

		if(!empty($var)){
			$result .= "&$var=$value";
		}else{
			writelog($session,"Empty option result [$serviceid] for $f_key", $filter, 'warning');
		}
	}

	return $result;
}
?>