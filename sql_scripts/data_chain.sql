SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for votedesc
-- ----------------------------
DROP TABLE IF EXISTS `data_chain`;
CREATE TABLE `data_chain` (
  `data_id` char(32) NOT NULL COMMENT '对应宁波id',
  `transaction_id` char(100) NOT NULL COMMENT '对应交易id',
  PRIMARY KEY (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='区块链关联表';