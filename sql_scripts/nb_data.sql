SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for vote_options
-- ----------------------------
DROP TABLE IF EXISTS `nb_data`;
CREATE TABLE `nb_data` (
  `data_id` char(32) NOT NULL COMMENT '宁波数据id',
  `create_user` char(50) NOT NULL COMMENT '创建者id',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `update_user` char(50) NOT NULL COMMENT '更新者id',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  `is_delete` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0未删除 1已删除',
  `task_id` char(32) DEFAULT NULL COMMENT '任务id',
  `task_def_key` char(32) DEFAULT NULL COMMENT '',
  `instance_id` char(32) DEFAULT NULL COMMENT '实例id',
  `task_name` char(32) DEFAULT NULL COMMENT '任务名称',
  `suspend_state` tinyint(1) unsigned  DEFAULT '0' COMMENT '',
  `current_user_list` char(32) DEFAULT NULL COMMENT '',
  `current_role_list` char(32) DEFAULT NULL COMMENT '',
  `current_user` char(50) DEFAULT NULL COMMENT '当前用户',
  `register_id` char(50) NOT NULL COMMENT '注册id',
  `register_name` char(50) NOT NULL COMMENT '注册名称',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `org_id` char(50) NOT NULL COMMENT '组织id',
  `org_name` char(50) NOT NULL COMMENT '组织名称',
  `display_mode` char(32) NOT NULL COMMENT '',
  `display_time` datetime NOT NULL COMMENT '',
  `public_time` datetime NOT NULL COMMENT '公开时间',
  `title` char(50) NOT NULL COMMENT '题目',
  `content` char(100) NOT NULL COMMENT '内容',
  `remark` char(50) NOT NULL COMMENT '评论',
  `state` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '',
  `fileUpload` char(255) NOT NULL COMMENT '',
  PRIMARY KEY (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宁波数据表';
