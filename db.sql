/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80039
 Source Host           : localhost:3306
 Source Schema         : springbootiv1oo

 Target Server Type    : MySQL
 Target Server Version : 80039
 File Encoding         : 65001

 Date: 09/12/2024 23:19:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '配置参数名称',
  `value` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '配置参数值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '配置文件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, 'picture1', 'http://localhost:8080/springbootiv1oo/upload/picture1.jpg');
INSERT INTO `config` VALUES (2, 'picture2', 'http://localhost:8080/springbootiv1oo/upload/picture2.jpg');
INSERT INTO `config` VALUES (3, 'picture3', 'http://localhost:8080/springbootiv1oo/upload/picture3.jpg');
INSERT INTO `config` VALUES (6, 'homepage', NULL);

-- ----------------------------
-- Table structure for gangweifenlei
-- ----------------------------
DROP TABLE IF EXISTS `gangweifenlei`;
CREATE TABLE `gangweifenlei`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gangweifenlei` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '岗位分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '岗位分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gangweifenlei
-- ----------------------------
INSERT INTO `gangweifenlei` VALUES (31, '2021-04-12 22:14:10', '服务类');
INSERT INTO `gangweifenlei` VALUES (32, '2021-04-12 22:14:10', '岗位分类2');
INSERT INTO `gangweifenlei` VALUES (33, '2021-04-12 22:14:10', '岗位分类3');
INSERT INTO `gangweifenlei` VALUES (34, '2021-04-12 22:14:10', '岗位分类4');
INSERT INTO `gangweifenlei` VALUES (35, '2021-04-12 22:14:10', '岗位分类5');
INSERT INTO `gangweifenlei` VALUES (36, '2021-04-12 22:14:10', '岗位分类6');

-- ----------------------------
-- Table structure for gangweishenqing
-- ----------------------------
DROP TABLE IF EXISTS `gangweishenqing`;
CREATE TABLE `gangweishenqing`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `zhaopingangwei` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '招聘岗位',
  `gangweifenlei` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '岗位分类',
  `xueliyaoqiu` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '学历要求',
  `xinzidaiyu` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '薪资待遇',
  `qiyezhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '企业账号',
  `qiyemingcheng` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `shenqingshijian` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `jianli` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '简历',
  `yonghuming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `yonghuxingming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `shoujihaoma` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `sfsh` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '否' COMMENT '是否审核',
  `shhf` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '审核回复',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1733674388897 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '岗位申请' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gangweishenqing
-- ----------------------------
INSERT INTO `gangweishenqing` VALUES (51, '2021-04-12 22:14:10', '招聘岗位1', '岗位分类1', '学历要求1', '薪资待遇1', '企业1', '企业名称1', '2021-04-12 22:14:10', '', '用户名1', '用户姓名1', '手机号码1', '是', '');
INSERT INTO `gangweishenqing` VALUES (52, '2021-04-12 22:14:10', '招聘岗位2', '岗位分类2', '学历要求2', '薪资待遇2', '企业2', '企业名称2', '2021-04-12 22:14:10', '', '用户名2', '用户姓名2', '手机号码2', '是', '');
INSERT INTO `gangweishenqing` VALUES (53, '2021-04-12 22:14:10', '招聘岗位3', '岗位分类3', '学历要求3', '薪资待遇3', '企业3', '企业名称3', '2021-04-12 22:14:10', '', '用户名3', '用户姓名3', '手机号码3', '是', '');
INSERT INTO `gangweishenqing` VALUES (54, '2021-04-12 22:14:10', '招聘岗位4', '岗位分类4', '学历要求4', '薪资待遇4', '企业4', '企业名称4', '2021-04-12 22:14:10', '', '用户名4', '用户姓名4', '手机号码4', '是', '');
INSERT INTO `gangweishenqing` VALUES (55, '2021-04-12 22:14:10', '招聘岗位5', '岗位分类5', '学历要求5', '薪资待遇5', '企业5', '企业名称5', '2021-04-12 22:14:10', '', '用户名5', '用户姓名5', '手机号码5', '是', '');
INSERT INTO `gangweishenqing` VALUES (56, '2021-04-12 22:14:10', '招聘岗位6', '岗位分类6', '学历要求6', '薪资待遇6', '企业6', '企业名称6', '2021-04-12 22:14:10', '', '用户名6', '用户姓名6', '手机号码6', '是', '');
INSERT INTO `gangweishenqing` VALUES (1618237646024, '2021-04-12 22:27:25', '文员', '岗位分类1', '大专以上', '3500-4500', '22', '江铃汽车', '2021-04-12 22:26:31', 'http://localhost:8080/springbootiv1oo/upload/1618237622971.doc', '11', '陈强', '13823855555', '是', '请于4月15号上午9点来公司面试');
INSERT INTO `gangweishenqing` VALUES (1733649916991, '2024-12-08 17:25:16', '招聘岗位3', '岗位分类3', '学历要求3', '薪资待遇3', '企业3', '企业名称3', '2024-12-08 00:00:00', NULL, '用户1', '用户姓名1', '13823888881', '是', NULL);
INSERT INTO `gangweishenqing` VALUES (1733650050606, '2024-12-08 17:27:30', '招聘岗位2', '岗位分类2', '学历要求2', '薪资待遇2', '企业2', '企业名称2', '2024-12-08 00:00:00', NULL, '用户1', '用户姓名1', '13823888881', '是', NULL);
INSERT INTO `gangweishenqing` VALUES (1733674388896, '2024-12-09 00:13:08', '招聘岗位1', '岗位分类1', '学历要求1', '薪资待遇1', '企业账号1', '企业名称1', '2024-12-09 00:00:00', NULL, 'jiang', '将好滴', '18557944838', '是', NULL);

-- ----------------------------
-- Table structure for qiuzhixinxi
-- ----------------------------
DROP TABLE IF EXISTS `qiuzhixinxi`;
CREATE TABLE `qiuzhixinxi`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `yonghuming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `yonghuxingming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `touxiang` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '头像',
  `shoujihaoma` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `qiuzhigangwei` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '求职岗位',
  `gongziyaoqiu` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '工资要求',
  `jianli` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '简历',
  `fabushijian` datetime NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1618237366928 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '求职信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qiuzhixinxi
-- ----------------------------
INSERT INTO `qiuzhixinxi` VALUES (71, '2021-04-12 22:14:10', '用户名1', '用户姓名1', 'http://localhost:8080/springbootiv1oo/upload/qiuzhixinxi_touxiang1.jpg', '手机号码1', '求职岗位1', '工资要求1', '', '2021-04-12 22:14:10');
INSERT INTO `qiuzhixinxi` VALUES (72, '2021-04-12 22:14:10', '用户名2', '用户姓名2', 'http://localhost:8080/springbootiv1oo/upload/qiuzhixinxi_touxiang2.jpg', '手机号码2', '求职岗位2', '工资要求2', '', '2021-04-12 22:14:10');
INSERT INTO `qiuzhixinxi` VALUES (73, '2021-04-12 22:14:10', '用户名3', '用户姓名3', 'http://localhost:8080/springbootiv1oo/upload/qiuzhixinxi_touxiang3.jpg', '手机号码3', '求职岗位3', '工资要求3', '', '2021-04-12 22:14:10');
INSERT INTO `qiuzhixinxi` VALUES (74, '2021-04-12 22:14:10', '用户名4', '用户姓名4', 'http://localhost:8080/springbootiv1oo/upload/qiuzhixinxi_touxiang4.jpg', '手机号码4', '求职岗位4', '工资要求4', '', '2021-04-12 22:14:10');
INSERT INTO `qiuzhixinxi` VALUES (75, '2021-04-12 22:14:10', '用户名5', '用户姓名5', 'http://localhost:8080/springbootiv1oo/upload/qiuzhixinxi_touxiang5.jpg', '手机号码5', '求职岗位5', '工资要求5', '', '2021-04-12 22:14:10');
INSERT INTO `qiuzhixinxi` VALUES (76, '2021-04-12 22:14:10', '用户名6', '用户姓名6', 'http://localhost:8080/springbootiv1oo/upload/qiuzhixinxi_touxiang6.jpg', '手机号码6', '求职岗位6', '工资要求6', '', '2021-04-12 22:14:10');
INSERT INTO `qiuzhixinxi` VALUES (1618237366927, '2021-04-12 22:22:46', '11', '陈强', 'http://localhost:8080/springbootiv1oo/upload/1618237169374.jpg', '13823855555', '文员', '4000以上', 'http://localhost:8080/springbootiv1oo/upload/1618237360977.doc', '2021-04-12 22:22:41');

-- ----------------------------
-- Table structure for qiye
-- ----------------------------
DROP TABLE IF EXISTS `qiye`;
CREATE TABLE `qiye`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `qiyezhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '企业账号',
  `mima` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `qiyemingcheng` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '企业名称',
  `tupian` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '图片',
  `lianxiren` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `lianxidianhua` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `qiyedizhi` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '企业地址',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `qiyezhanghao`(`qiyezhanghao`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1733652103901 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '企业' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qiye
-- ----------------------------
INSERT INTO `qiye` VALUES (21, '2021-04-12 22:14:10', '企业1', '123456', '企业名称1', 'http://localhost:8080/springbootiv1oo/upload/qiye_tupian1.jpg', '联系人1', '13823888881', '企业地址1');
INSERT INTO `qiye` VALUES (22, '2021-04-12 22:14:10', '企业2', '123456', '企业名称2', 'http://localhost:8080/springbootiv1oo/upload/qiye_tupian2.jpg', '联系人2', '13823888882', '企业地址2');
INSERT INTO `qiye` VALUES (23, '2021-04-12 22:14:10', '企业3', '123456', '企业名称3', 'http://localhost:8080/springbootiv1oo/upload/qiye_tupian3.jpg', '联系人3', '13823888883', '企业地址3');
INSERT INTO `qiye` VALUES (24, '2021-04-12 22:14:10', '企业4', '123456', '企业名称4', 'http://localhost:8080/springbootiv1oo/upload/qiye_tupian4.jpg', '联系人4', '13823888884', '企业地址4');
INSERT INTO `qiye` VALUES (25, '2021-04-12 22:14:10', '企业5', '123456', '企业名称5', 'http://localhost:8080/springbootiv1oo/upload/qiye_tupian5.jpg', '联系人5', '13823888885', '企业地址5');
INSERT INTO `qiye` VALUES (26, '2021-04-12 22:14:10', '企业6', '123456', '企业名称6', 'http://localhost:8080/springbootiv1oo/upload/qiye_tupian6.jpg', '联系人6', '13823888886', '企业地址6');
INSERT INTO `qiye` VALUES (1618237437882, '2021-04-12 22:23:57', '22', '123456', '江铃汽车', 'http://localhost:8080/springbootiv1oo/upload/1618237467544.jpg', '巫先生', '13823899999', '广州路9号');
INSERT INTO `qiye` VALUES (1733652103900, '2024-12-08 18:01:43', '314141', '314141', '314141', NULL, NULL, '19857944838', NULL);

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint NOT NULL COMMENT '用户id',
  `username` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `tablename` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '表名',
  `role` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色',
  `token` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = 'token表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of token
-- ----------------------------
INSERT INTO `token` VALUES (1, 1618237144568, '11', 'yonghu', '用户', '50vucv8qevg1hjhd5zq6q8cwfvpnf575', '2021-04-12 22:19:13', '2021-04-12 23:32:44');
INSERT INTO `token` VALUES (2, 1618237437882, '22', 'qiye', '企业', '0o1auab8aumdy1qdvmi8e3tiftxyk8lm', '2021-04-12 22:24:06', '2021-04-12 23:28:15');
INSERT INTO `token` VALUES (3, 21, '企业1', 'qiye', '企业', '5nrvw680r0tv166pu1jfhpr7uj7e3o8d', '2021-04-12 22:30:53', '2024-12-09 01:23:07');
INSERT INTO `token` VALUES (4, 1, 'abo', 'users', '管理员', 'tbjbi2945qix32atrenfi206yqftthpc', '2021-04-12 22:33:02', '2024-12-09 01:16:01');
INSERT INTO `token` VALUES (5, 1731340025917, 'jiang', 'yonghu', '用户', '1gapv06lqus5w7wcaecqtvt9vldc6dlk', '2024-11-11 23:48:35', '2024-12-09 01:19:51');
INSERT INTO `token` VALUES (6, 11, '用户1', 'yonghu', '用户', 'fziuh4iy3mdq94pu3o35jpqd2jnobu2h', '2024-12-08 17:19:45', '2024-12-09 01:18:57');
INSERT INTO `token` VALUES (7, 1733652103900, '314141', 'qiye', '企业', '9u00cvu8rp96nw5wcaypxsvp79tiyxbc', '2024-12-08 18:01:49', '2024-12-09 01:08:51');
INSERT INTO `token` VALUES (8, 1733673957377, 'jiang1', 'yonghu', '用户', 'sg46aapccg8fpfi147lopc1ow78qio7h', '2024-12-09 00:06:04', '2024-12-09 01:10:00');
INSERT INTO `token` VALUES (9, 23, '企业3', 'qiye', '企业', 'bklen7bdqfx94p8kkb2f1crsyysatauf', '2024-12-09 00:09:30', '2024-12-09 01:27:00');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `role` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '管理员' COMMENT '角色',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'abo', 'abo', '管理员', '2021-04-12 22:14:10');

-- ----------------------------
-- Table structure for yaoqingmianshi
-- ----------------------------
DROP TABLE IF EXISTS `yaoqingmianshi`;
CREATE TABLE `yaoqingmianshi`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `yonghuming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `yonghuxingming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `shoujihaoma` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `qiuzhigangwei` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '求职岗位',
  `yaoqingshijian` datetime NULL DEFAULT NULL COMMENT '邀请时间',
  `neirong` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '内容',
  `qiyezhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '企业账号',
  `qiyemingcheng` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `lianxiren` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `lianxidianhua` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `qiyedizhi` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '企业地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1733650115666 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '邀请面试' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of yaoqingmianshi
-- ----------------------------
INSERT INTO `yaoqingmianshi` VALUES (81, '2021-04-12 22:14:10', '用户名1', '用户姓名1', '手机号码1', '求职岗位1', '2021-04-12 22:14:10', '内容1', '企业账号1', '企业名称1', '联系人1', '联系电话1', '企业地址1');
INSERT INTO `yaoqingmianshi` VALUES (82, '2021-04-12 22:14:10', '用户名2', '用户姓名2', '手机号码2', '求职岗位2', '2021-04-12 22:14:10', '内容2', '企业账号2', '企业名称2', '联系人2', '联系电话2', '企业地址2');
INSERT INTO `yaoqingmianshi` VALUES (83, '2021-04-12 22:14:10', '用户名3', '用户姓名3', '手机号码3', '求职岗位3', '2021-04-12 22:14:10', '内容3', '企业账号3', '企业名称3', '联系人3', '联系电话3', '企业地址3');
INSERT INTO `yaoqingmianshi` VALUES (84, '2021-04-12 22:14:10', '用户名4', '用户姓名4', '手机号码4', '求职岗位4', '2021-04-12 22:14:10', '内容4', '企业账号4', '企业名称4', '联系人4', '联系电话4', '企业地址4');
INSERT INTO `yaoqingmianshi` VALUES (85, '2021-04-12 22:14:10', '用户名5', '用户姓名5', '手机号码5', '求职岗位5', '2021-04-12 22:14:10', '内容5', '企业账号5', '企业名称5', '联系人5', '联系电话5', '企业地址5');
INSERT INTO `yaoqingmianshi` VALUES (86, '2021-04-12 22:14:10', '用户名6', '用户姓名6', '手机号码6', '求职岗位6', '2021-04-12 22:14:10', '内容6', '企业账号6', '企业名称6', '联系人6', '联系电话6', '企业地址6');
INSERT INTO `yaoqingmianshi` VALUES (1618237935804, '2021-04-12 22:32:15', '11', '陈强', '13823855555', '文员', '2021-04-15 09:00:00', '邀请你于15号上午来我公司面试', '企业1', '企业名称1', '联系人1', '13823888881', '企业地址1');
INSERT INTO `yaoqingmianshi` VALUES (1733650115665, '2024-12-08 17:28:35', '用户名2', '用户姓名2', '手机号码2', '求职岗位2', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for yonghu
-- ----------------------------
DROP TABLE IF EXISTS `yonghu`;
CREATE TABLE `yonghu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `yonghuming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `mima` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `yonghuxingming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户姓名',
  `touxiang` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '头像',
  `xingbie` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '性别',
  `nianling` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '年龄',
  `shoujihaoma` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `youxiang` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `yonghuming`(`yonghuming`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1733673957378 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of yonghu
-- ----------------------------
INSERT INTO `yonghu` VALUES (11, '2021-04-12 22:14:10', '用户1', '123456', '用户姓名1', 'http://localhost:8080/springbootiv1oo/upload/yonghu_touxiang1.jpg', '男', '年龄1', '13823888881', '773890001@qq.com');
INSERT INTO `yonghu` VALUES (12, '2021-04-12 22:14:10', '用户2', '123456', '用户姓名2', 'http://localhost:8080/springbootiv1oo/upload/yonghu_touxiang2.jpg', '男', '年龄2', '13823888882', '773890002@qq.com');
INSERT INTO `yonghu` VALUES (13, '2021-04-12 22:14:10', '用户3', '123456', '用户姓名3', 'http://localhost:8080/springbootiv1oo/upload/yonghu_touxiang3.jpg', '男', '年龄3', '13823888883', '773890003@qq.com');
INSERT INTO `yonghu` VALUES (14, '2021-04-12 22:14:10', '用户4', '123456', '用户姓名4', 'http://localhost:8080/springbootiv1oo/upload/yonghu_touxiang4.jpg', '男', '年龄4', '13823888884', '773890004@qq.com');
INSERT INTO `yonghu` VALUES (15, '2021-04-12 22:14:10', '用户5', '123456', '用户姓名5', 'http://localhost:8080/springbootiv1oo/upload/yonghu_touxiang5.jpg', '男', '年龄5', '13823888885', '773890005@qq.com');
INSERT INTO `yonghu` VALUES (16, '2021-04-12 22:14:10', '用户6', '123456', '用户姓名6', 'http://localhost:8080/springbootiv1oo/upload/yonghu_touxiang6.jpg', '男', '年龄6', '13823888886', '773890006@qq.com');
INSERT INTO `yonghu` VALUES (1618237144568, '2021-04-12 22:19:04', '11', '123456', '陈强', 'http://localhost:8080/springbootiv1oo/upload/1618237169374.jpg', '男', '25', '13823855555', NULL);
INSERT INTO `yonghu` VALUES (1731340025917, '2024-11-11 23:47:05', 'jiang', '314141', '将好滴', NULL, NULL, NULL, '18557944838', '1018576890@qq.com');
INSERT INTO `yonghu` VALUES (1733673957377, '2024-12-09 00:05:57', 'jiang1', '314141', 'jianghaodi', NULL, NULL, '22', '18557944818', '1018576890@qq.com');

-- ----------------------------
-- Table structure for zaixianliuyan
-- ----------------------------
DROP TABLE IF EXISTS `zaixianliuyan`;
CREATE TABLE `zaixianliuyan`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `zhaopingangwei` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '招聘岗位',
  `gangweifenlei` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '岗位分类',
  `xueliyaoqiu` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '学历要求',
  `xinzidaiyu` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '薪资待遇',
  `qiyezhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '企业账号',
  `qiyemingcheng` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `liuyanneirong` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '留言内容',
  `liuyanshijian` datetime NULL DEFAULT NULL COMMENT '留言时间',
  `yonghuming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `yonghuxingming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `shoujihaoma` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `sfsh` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '否' COMMENT '是否审核',
  `shhf` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '审核回复',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1733674599072 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '在线留言' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zaixianliuyan
-- ----------------------------
INSERT INTO `zaixianliuyan` VALUES (61, '2021-04-12 22:14:10', '招聘岗位1', '岗位分类1', '学历要求1', '薪资待遇1', '企业1', '企业名称1', '留言内容1', '2021-04-12 22:14:10', '用户名1', '用户姓名1', '手机号码1', '是', '');
INSERT INTO `zaixianliuyan` VALUES (62, '2021-04-12 22:14:10', '招聘岗位2', '岗位分类2', '学历要求2', '薪资待遇2', '企业2', '企业名称2', '留言内容2', '2021-04-12 22:14:10', '用户名2', '用户姓名2', '手机号码2', '是', '');
INSERT INTO `zaixianliuyan` VALUES (63, '2021-04-12 22:14:10', '招聘岗位3', '岗位分类3', '学历要求3', '薪资待遇3', '企业3', '企业名称3', '留言内容3', '2021-04-12 22:14:10', '用户名3', '用户姓名3', '手机号码3', '是', '');
INSERT INTO `zaixianliuyan` VALUES (64, '2021-04-12 22:14:10', '招聘岗位4', '岗位分类4', '学历要求4', '薪资待遇4', '企业4', '企业名称4', '留言内容4', '2021-04-12 22:14:10', '用户名4', '用户姓名4', '手机号码4', '是', '');
INSERT INTO `zaixianliuyan` VALUES (65, '2021-04-12 22:14:10', '招聘岗位5', '岗位分类5', '学历要求5', '薪资待遇5', '企业5', '企业名称5', '留言内容5', '2021-04-12 22:14:10', '用户名5', '用户姓名5', '手机号码5', '是', '');
INSERT INTO `zaixianliuyan` VALUES (66, '2021-04-12 22:14:10', '招聘岗位6', '岗位分类6', '学历要求6', '薪资待遇6', '企业6', '企业名称6', '留言内容6', '2021-04-12 22:14:10', '用户名6', '用户姓名6', '手机号码6', '是', '');
INSERT INTO `zaixianliuyan` VALUES (1618237665143, '2021-04-12 22:27:44', '文员', '岗位分类1', '大专以上', '3500-4500', '22', '江铃汽车', 'sdfsfsdsgsdgsg', '2021-04-12 22:27:36', '11', '陈强', '13823855555', '是', 'sdfsgsdgsgg');
INSERT INTO `zaixianliuyan` VALUES (1733653354352, '2024-12-08 18:22:33', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '否', NULL);
INSERT INTO `zaixianliuyan` VALUES (1733674014706, '2024-12-09 00:06:54', '招聘岗位3', '岗位分类3', '学历要求3', '薪资待遇3', '企业3', '企业名称3', '111', '2024-12-08 00:00:00', 'jiang1', 'jianghaodi', '18557944818', '否', NULL);
INSERT INTO `zaixianliuyan` VALUES (1733674599071, '2024-12-09 00:16:38', '招聘岗位1', '岗位分类1', '学历要求1', '薪资待遇1', '企业1', '企业名称1', '123', NULL, 'jiang', '将好滴', '18557944838', '否', NULL);

-- ----------------------------
-- Table structure for zhaopinxinxi
-- ----------------------------
DROP TABLE IF EXISTS `zhaopinxinxi`;
CREATE TABLE `zhaopinxinxi`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `zhaopingangwei` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '招聘岗位',
  `tupian` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '图片',
  `gangweifenlei` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '岗位分类',
  `zhaopinrenshu` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '招聘人数',
  `xueliyaoqiu` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '学历要求',
  `xinzidaiyu` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '薪资待遇',
  `gongzuoshijian` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '工作时间',
  `gongzuoneirong` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '工作内容',
  `gangweixiangqing` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '岗位详情',
  `qiyezhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '企业账号',
  `qiyemingcheng` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `lianxiren` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `lianxidianhua` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `qiyedizhi` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '企业地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1618237542572 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '招聘信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zhaopinxinxi
-- ----------------------------
INSERT INTO `zhaopinxinxi` VALUES (41, '2021-04-12 22:14:10', '招聘岗位1', 'http://localhost:8080/springbootiv1oo/upload/zhaopinxinxi_tupian1.jpg', '岗位分类1', '招聘人数1', '学历要求1', '薪资待遇1', '工作时间1', '工作内容1', '岗位详情1', '企业账号1', '企业名称1', '联系人1', '联系电话1', '企业地址1');
INSERT INTO `zhaopinxinxi` VALUES (42, '2021-04-12 22:14:10', '招聘岗位2', 'http://localhost:8080/springbootiv1oo/upload/zhaopinxinxi_tupian2.jpg', '岗位分类2', '招聘人数2', '学历要求2', '薪资待遇2', '工作时间2', '工作内容2', '岗位详情2', '企业账号2', '企业名称2', '联系人2', '联系电话2', '企业地址2');
INSERT INTO `zhaopinxinxi` VALUES (43, '2021-04-12 22:14:10', '招聘岗位3', 'http://localhost:8080/springbootiv1oo/upload/zhaopinxinxi_tupian3.jpg', '岗位分类3', '招聘人数3', '学历要求3', '薪资待遇3', '工作时间3', '工作内容3', '岗位详情3', '企业账号3', '企业名称3', '联系人3', '联系电话3', '企业地址3');
INSERT INTO `zhaopinxinxi` VALUES (44, '2021-04-12 22:14:10', '招聘岗位4', 'http://localhost:8080/springbootiv1oo/upload/zhaopinxinxi_tupian4.jpg', '岗位分类4', '招聘人数4', '学历要求4', '薪资待遇4', '工作时间4', '工作内容4', '岗位详情4', '企业账号4', '企业名称4', '联系人4', '联系电话4', '企业地址4');
INSERT INTO `zhaopinxinxi` VALUES (45, '2021-04-12 22:14:10', '招聘岗位5', 'http://localhost:8080/springbootiv1oo/upload/zhaopinxinxi_tupian5.jpg', '岗位分类5', '招聘人数5', '学历要求5', '薪资待遇5', '工作时间5', '工作内容5', '岗位详情5', '企业账号5', '企业名称5', '联系人5', '联系电话5', '企业地址5');
INSERT INTO `zhaopinxinxi` VALUES (46, '2021-04-12 22:14:10', '招聘岗位6', 'http://localhost:8080/springbootiv1oo/upload/zhaopinxinxi_tupian6.jpg', '岗位分类6', '招聘人数6', '学历要求6', '薪资待遇6', '工作时间6', '工作内容6', '岗位详情6', '企业账号6', '企业名称6', '联系人6', '联系电话6', '企业地址6');
INSERT INTO `zhaopinxinxi` VALUES (1618237542571, '2021-04-12 22:25:42', '文员', 'http://localhost:8080/springbootiv1oo/upload/1618237508290.jpg', '岗位分类1', '2', '大专以上', '3500-4500', '8小时', 'sdfsfsdfsdfsf', '<p>sdfsdgsdgsdfsdfsdgsdgsdgff</p>', '22', '江铃汽车', '巫先生', '13823899999', '广州路9号');

SET FOREIGN_KEY_CHECKS = 1;
