# HeidiSQL Dump 
#
# --------------------------------------------------------
# Host:                         127.0.0.1
# Database:                     carcw
# Server version:               5.0.51b-community
# Server OS:                    Win32
# Target compatibility:         HeidiSQL w/ MySQL Server 3.23
# Target max_allowed_packet:    1047552
# HeidiSQL version:             4.0
# Date/time:                    2010-01-22 23:50:49
# --------------------------------------------------------

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;*/


#
# Database structure for database 'carcw'
#

DROP DATABASE IF EXISTS `carcw`;
CREATE DATABASE `carcw` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `carcw`;


#
# Table structure for table 'core_download'
#

CREATE TABLE `core_download` (
  `id` int(10) unsigned default NULL,
  `code` varchar(32) default NULL,
  `expiration` datetime default NULL,
  KEY `id` (`id`),
  CONSTRAINT `core_download_ibfk_1` FOREIGN KEY (`id`) REFERENCES `core_instance` (`id`) ON DELETE CASCADE
) TYPE=InnoDB;



#
# Table structure for table 'core_instance'
#

CREATE TABLE `core_instance` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `email` varchar(256) default NULL,
  `iid` varchar(32) default NULL,
  `registration` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `expiration` timestamp NULL default NULL,
  `ip` varchar(15) default NULL,
  PRIMARY KEY  (`id`)
) TYPE=InnoDB AUTO_INCREMENT=6;



#
# Table structure for table 'core_log'
#

CREATE TABLE `core_log` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `ip` varchar(15) default NULL,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `sessionid` varchar(32) default NULL,
  `action` varchar(512) default NULL,
  `level` set('info','warning','error') NOT NULL default 'info',
  `data` varchar(512) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`)
) TYPE=InnoDB AUTO_INCREMENT=477;



#
# Table structure for table 'master_make'
#

CREATE TABLE `master_make` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) TYPE=InnoDB AUTO_INCREMENT=86;



#
# Table structure for table 'master_model'
#

CREATE TABLE `master_model` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `makeid` int(10) unsigned NOT NULL,
  `name` varchar(50) default NULL,
  PRIMARY KEY  (`id`),
  KEY `makeIdx` (`makeid`),
  CONSTRAINT `master_model_ibfk_1` FOREIGN KEY (`makeid`) REFERENCES `master_make` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB AUTO_INCREMENT=1578;



#
# Table structure for table 'master_option'
#

CREATE TABLE `master_option` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(50) default NULL,
  `through` enum('0','1') default '0',
  PRIMARY KEY  (`id`)
) TYPE=InnoDB AUTO_INCREMENT=16;



#
# Table structure for table 'master_optionvalue'
#

CREATE TABLE `master_optionvalue` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `optionid` int(10) unsigned NOT NULL,
  `value` varchar(50) default NULL,
  PRIMARY KEY  (`id`),
  KEY `optionIdx` (`optionid`),
  CONSTRAINT `master_optionvalue_ibfk_1` FOREIGN KEY (`optionid`) REFERENCES `master_option` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB AUTO_INCREMENT=212;



#
# Table structure for table 'service'
#

CREATE TABLE `service` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(50) default NULL,
  `prefix` varchar(512) default NULL,
  `period` int(10) unsigned default NULL,
  PRIMARY KEY  (`id`)
) TYPE=InnoDB AUTO_INCREMENT=6;



#
# Table structure for table 'service_make'
#

CREATE TABLE `service_make` (
  `masterid` int(10) unsigned NOT NULL default '0',
  `serviceid` int(10) unsigned NOT NULL default '0',
  `value` varchar(50) default NULL,
  PRIMARY KEY  (`masterid`,`serviceid`),
  KEY `valueIdx` (`value`),
  KEY `serviceid` (`serviceid`),
  CONSTRAINT `service_make_ibfk_2` FOREIGN KEY (`masterid`) REFERENCES `master_make` (`id`) ON DELETE CASCADE,
  CONSTRAINT `service_make_ibfk_3` FOREIGN KEY (`serviceid`) REFERENCES `service` (`id`) ON DELETE CASCADE
) TYPE=InnoDB;



#
# Table structure for table 'service_model'
#

CREATE TABLE `service_model` (
  `masterid` int(10) unsigned NOT NULL default '0',
  `serviceid` int(10) unsigned NOT NULL default '0',
  `value` varchar(50) default NULL,
  PRIMARY KEY  (`masterid`,`serviceid`),
  KEY `valueIdx` (`value`),
  KEY `serviceid` (`serviceid`),
  CONSTRAINT `service_model_ibfk_2` FOREIGN KEY (`masterid`) REFERENCES `master_model` (`id`) ON DELETE CASCADE,
  CONSTRAINT `service_model_ibfk_3` FOREIGN KEY (`serviceid`) REFERENCES `service` (`id`) ON DELETE CASCADE
) TYPE=InnoDB;



#
# Table structure for table 'service_option'
#

CREATE TABLE `service_option` (
  `masterid` int(10) unsigned NOT NULL default '0',
  `serviceid` int(10) unsigned NOT NULL default '0',
  `value` varchar(50) default NULL,
  PRIMARY KEY  (`masterid`,`serviceid`),
  KEY `valueIdx` (`value`),
  KEY `serviceid` (`serviceid`),
  CONSTRAINT `service_option_ibfk_1` FOREIGN KEY (`masterid`) REFERENCES `master_option` (`id`) ON DELETE CASCADE,
  CONSTRAINT `service_option_ibfk_2` FOREIGN KEY (`serviceid`) REFERENCES `service` (`id`) ON DELETE CASCADE
) TYPE=InnoDB;



#
# Table structure for table 'service_optionvalue'
#

CREATE TABLE `service_optionvalue` (
  `masterid` int(10) unsigned NOT NULL default '0',
  `serviceid` int(10) unsigned NOT NULL default '0',
  `value` varchar(50) default NULL,
  PRIMARY KEY  (`masterid`,`serviceid`),
  KEY `valueIdx` (`value`),
  KEY `serviceid` (`serviceid`),
  CONSTRAINT `service_optionvalue_ibfk_1` FOREIGN KEY (`masterid`) REFERENCES `master_optionvalue` (`id`) ON DELETE CASCADE,
  CONSTRAINT `service_optionvalue_ibfk_2` FOREIGN KEY (`serviceid`) REFERENCES `service` (`id`) ON DELETE CASCADE
) TYPE=InnoDB;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;*/
