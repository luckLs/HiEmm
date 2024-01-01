/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 80016 (8.0.16)
 Source Host           : localhost:3306
 Source Schema         : emm

 Target Server Type    : MySQL
 Target Server Version : 80016 (8.0.16)
 File Encoding         : 65001

 Date: 18/12/2023 21:45:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for db_data_base_info
-- ----------------------------
DROP TABLE IF EXISTS `db_data_base_info`;
CREATE TABLE `db_data_base_info`
(
    `id`           int(11)                                                       NOT NULL AUTO_INCREMENT COMMENT '库ID',
    `name`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '库名',
    `type`         varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '数据库类型',
    `connect_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '连接名',
    `Jdbc_url`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '连接地址',
    `description`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NULL COMMENT '描述',
    `user_name`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
    `password`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
    `set_fk_rule`  varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '外键命名规则【 _id：表名_id 】、【id：表名id】',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据库信息'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for db_column
-- ----------------------------
DROP TABLE IF EXISTS `db_column`;
CREATE TABLE `db_column`
(
    `id`               int(11)                                                       NOT NULL AUTO_INCREMENT COMMENT '列ID',
    `database_info_id` int(11)                                                       NULL     DEFAULT NULL COMMENT '库id',
    `table_id`         int(11)                                                       NOT NULL COMMENT '所属表 ID',
    `foreign_table_id` int(11)                                                       NULL     DEFAULT NULL COMMENT '外键表id',
    `foreign_key_id`   int(11)                                                       NULL     DEFAULT NULL COMMENT '外键id',
    `name`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '列名',
    `data_type`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '数据类型',
    `is_primary_key`   tinyint(1)                                                    NOT NULL DEFAULT 0 COMMENT '是否为主键',
    `alias`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '列的别名',
    `description`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '描述',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 690
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '列信息'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for db_table
-- ----------------------------
DROP TABLE IF EXISTS `db_table`;
CREATE TABLE `db_table`
(
    `id`               int(11)                                                       NOT NULL AUTO_INCREMENT COMMENT '表ID',
    `database_info_id` int(11)                                                       NULL DEFAULT NULL COMMENT '库id',
    `name`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表名',
    `alias`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表的别名',
    `description`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NULL COMMENT '描述',
    `ai_corpus`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'ai语料',
    `position_x`       int(11)                                                       NULL DEFAULT NULL COMMENT '坐标X',
    `position_y`       int(11)                                                       NULL DEFAULT NULL COMMENT '坐标Y',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 57
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '表信息'
  ROW_FORMAT = Dynamic;

-- ----------------------------
