<?php
/**
 * allgemeine Konstanten
 */

$sql_servername = "localhost";
$sql_user ="http";
$sql_passwd="didsgP!";
$sql_db = "carcw";

$SQL = new Database($sql_servername,$sql_user,$sql_passwd,$sql_db);

/**
 * Klasse für den Zugriff auf eine MySQL-Datenbank
 *
 * Diese Klasse fungiert als Midtier für den Zugriff auf eine
 * MySQL-Datenbank und optimiert den Zugriff auf diese.
 * Ferner gehört zu den Aufgaben dieser Klasse eine sinnvolle
 * Array-Behandlung unter Rücksichtnahme auf Smarty und eine
 * optimierte Fehlerbehandlung.
 *
 * History:<br>
 * ----------------------------------------------------------------<br>
 * (1.00) - 30.03.2007 -  Ersterstellung
 *
 * @package    Database
 * @author     Guido Mühlwitz <gm@terramedia.de>
 * @copyright  Copyright (c) 2007, terramedia gmbh
 * @version    1.00
 */

class Database {
	/**
	 * Name des aktuell verwendeten Datenbankservers
	 *
	 * @var string
	 * @access private
	 */
	private $db_server = "";
	/**
	 * Username für den Zugriff auf den aktuellen Datenbankserver
	 *
	 * @var string
	 * @access private
	 */
	private $db_username = "";
	/**
	 * Passwort des aktuell verwendeten Datenbankservers
	 *
	 * @var string
	 * @access private
	 */
	private $db_password = "";
	/**
	 * Aktuelle Datenbank des aktuell verwendeten Datenbankservers
	 *
	 * @var string
	 * @access private
	 */
	private $db_database = "";



	/**
	 * Database::constructor()
	 *
	 * Setzt die Verbindungsdaten, stellt eine Verbindung zum Server her und wählt Datenbank aus.
	 *
	 * @param string $db_server Servername (z.B. localhost)
	 * @param string $db_username Username
	 * @param string $db_password Passwort
	 * @param string $db_database Datenbank
	 * @throws DatabaseException
	 * @return void
	 */
	function __construct ( $db_server, $db_username, $db_password, $db_database ) {
		$this->db_server = $db_server;
		$this->db_username = $db_username;
		$this->db_password = $db_password;
		$this->db_database = $db_database;
		// Ausreichend Werte gesetzt?
		if( $db_server == "" || $db_username == "" || $db_password == "" || $db_database == "" ) {
			throw new DatabaseException("Class database::construct -> Unzureichende Parameterübergabe.");
		} else {
			$this->connect();
			$this->select_db();
		}
	}

	/**
	 * Database::connect()
	 *
	 * Stellt eine Verbindung zum Datenbankserver her und setzt Defaults
	 *
	 * @throws DatabaseException
	 * @return void
	 */
	function connect () {
		$result = mysql_pconnect( $this->db_server, $this->db_username, $this->db_password );
		if( $result === false  ) {
			throw new DatabaseException("Class database::connect -> ".mysql_errno()." - ".mysql_error());
		}
		//
	}

	/**
	 * Database::select_db()
	 *
	 * Wählt eine Datenbank innerhalb des Datenbankservers aus
	 *
	 * @throws DatabaseException
	 * @return void
	 */
	function select_db() {
		$result = mysql_select_db( $this->db_database );
		if( !$result ) {
			throw new DatabaseException("Class database::select_db -> ".mysql_errno()." - ".mysql_error());
		}
	}

	/**
	 * Database::insert()
	 *
	 * Wählt eine Datenbank innerhalb des Datenbankservers aus
	 *
	 * @param string Der Name der Tabelle
	 * @param array Der Feldname als key, das entsprechende Value als val
	 * @return boolean
	 */
	function insert( $table, $data ) {
		$keyarray = array();
		$valarray = array();
		foreach( $data as $key => $val ) {
			$keyarray[] = $key;
			$valarray[] = $val;
		}
		//
		$keystr = implode(", ", $keyarray);
		$valstr = implode(", ", $valarray);
		//		print ("INSERT INTO $table ($keystr) VALUES ($valstr)");
		$result = mysql_query("INSERT INTO $table ($keystr) VALUES ($valstr)");
		if( !$result ) {
			throw new DatabaseException("Class database::insert -> ".mysql_errno()." - ".mysql_error());
		}
	}

	/**
	 * Database::update()
	 *
	 * Aktualisiert den Wert innerhalb einer Spalte der Datenbank
	 *
	 * @param string Der Name der Tabelle
	 * @param string Where-Regel
	 * @param string Update-Befehl
	 * @return boolean
	 */
	function update( $table, $where, $rule ) {

		//
		$result = mysql_query("UPDATE $table SET $rule WHERE $where");
		if( !$result ) {
			throw new DatabaseException("Class database::update -> ".mysql_errno()." - ".mysql_error());
		}
		return mysql_affected_rows();
	}

	/**
	 * Database::delete()
	 *
	 * Löschte Werte innerhalb einer Datenbank
	 *
	 * @param string Der Name der Tabelle
	 * @param string Where-Regel
	 * @return boolean
	 */
	function delete( $table, $where ) {

		//
		$result = mysql_query("DELETE FROM $table WHERE $where");
		if( !$result ) {
			throw new DatabaseException("Class database::delete -> ".mysql_errno()." - ".mysql_error());
		}
		return mysql_affected_rows();
	}

	/**
	 * Database::getAll()
	 *
	 * Gibt alle gefundenen Rows einer Query als Array zurück
	 *
	 * @param string Die auszuführende Query
	 * @return array
	 */
	function getAll( $query ) {
		$result = mysql_query( $query );
		if( !$result ) {
			throw new DatabaseException("Class database::getAll -> ".mysql_errno()." - ".mysql_error());
		}
		//
		$resultarray = array();
		while( $row = mysql_fetch_assoc( $result ) ) {
			$resultarray[] = $row;
		}
		//
		mysql_free_result($result);
		return $resultarray;
	}

	/**
	 * Database::getOne()
	 *
	 * Gibt die erste gefundene Rows einer Query als Array zurück
	 *
	 * @param string Die auszuführende Query
	 * @return array
	 */
	function getOne( $query ) {
		$result = mysql_query( $query );
		if( !$result ) {
			throw new DatabaseException("Class database::getOne -> ".mysql_errno()." - ".mysql_error());
		}
		//
		$row = mysql_fetch_assoc( $result );
		//
		mysql_free_result($result);
		return $row;
	}

	/**
	 * Database::query()
	 *
	 * Eine ungefilterte Anfrage an DB
	 *
	 * @param string Die auszuführende Query
	 * @return
	 */
	function query( $query ) {
		$result = mysql_query( $query );
		if( !$result ) {
			throw new DatabaseException("Class database::getOne -> ".mysql_errno()." - ".mysql_error());
		}

		return $result;
	}
}


/**
 * Exception-Handler für Datenbanken
 *
 * Vererbung des Standard-Exception-Handlers ohne besondere Aufgaben
 *
 * History:<br>
 * ----------------------------------------------------------------<br>
 * (1.00) - 02.04.2007 -  Ersterstellung
 *
 * @package    Database
 * @author     Guido Mühlwitz <gm@terramedia.de>
 * @copyright  Copyright (c) 2007, terramedia gmbh
 * @version    1.00
 */
class DatabaseException extends Exception{};

function dbstring( $string) {
	return '"'. mysql_real_escape_string($string).'"';
}

?>