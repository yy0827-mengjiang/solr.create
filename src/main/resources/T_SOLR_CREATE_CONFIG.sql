/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.91-mysql
Source Server Version : 50629
Source Host           : 192.168.1.91:3306
Source Database       : qbyp-plus

Target Server Type    : MYSQL
Target Server Version : 50629
File Encoding         : 65001

Date: 2018-12-05 17:05:28
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for T_SOLR_CREATE_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `T_SOLR_CREATE_CONFIG`;
CREATE TABLE `T_SOLR_CREATE_CONFIG` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `COLLECTION_NAME` varchar(64) NOT NULL COMMENT 'COLLECTION名称',
  `CONFIG_NAME` varchar(32) NOT NULL COMMENT '配置文件注册zookeeper名称',
  `NUM_SHARDS` int(11) NOT NULL COMMENT 'shard个数',
  `REPLICATION_FACTOR` int(11) NOT NULL COMMENT '副本个数',
  `IS_CREATED` tinyint(1) NOT NULL COMMENT '是否已创建(0代表false未创建；1是true已创建)',
  `BEGIN_MONTH` bigint(20) NOT NULL COMMENT '开始时间',
  `END_MONTH` bigint(20) NOT NULL COMMENT '结束时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=666 DEFAULT CHARSET=utf8 COMMENT='SOLR创建的collection信息';
