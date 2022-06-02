CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(64)  NOT NULL,
  `age` smallint DEFAULT '1',
  `addr` varchar(256)  DEFAULT NULL,
  PRIMARY KEY (`id`)
)