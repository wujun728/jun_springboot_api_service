CREATE TABLE `calculate_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `interface_id` varchar(128) NOT NULL COMMENT 'interface_id',
  `bean_name` varchar(64) NOT NULL COMMENT 'bean_name',
  `calculate_rule` text NOT NULL COMMENT 'calculate_rule',
  `calculate_type` varchar(64) NOT NULL COMMENT 'reward',
  `status` varchar(16) NOT NULL DEFAULT 'ENABLE' COMMENT 'ENABLE/DISENABLE',
  `extend_info` varchar(4096) DEFAULT NULL,
  `created_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `modified_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='calculate rule';


https://blog.csdn.net/qq_33101675/article/details/107156878