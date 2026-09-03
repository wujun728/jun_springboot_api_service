insert into `SQLREST_SYSTEM_USER`(`username`,`password`,`salt`,`real_name`,`locked`,`email`)
VALUES ('admin', '$2a$10$eUanVjvzV27BBxAb4zuBCugwnngHkRZ7ZB4iI5tdx9ETJ2tnXJJDy', '$2a$10$eUanVjvzV27BBxAb4zuBCu', '管理员', 0,'admin@126.com');

INSERT INTO `SQLREST_FIREWALL_RULES` (`id`, `status`, `mode`)
VALUES ('1', 'OFF', 'BLACK');

INSERT INTO `SQLREST_API_GROUP` (`id`, `name`)
VALUES ('1', '默认分组');

INSERT INTO `SQLREST_API_MODULE` (`id`, `name`)
VALUES ('1', '默认模块');

INSERT INTO `SQLREST_APP_CLIENT` (`id`, `name`, `description`, `app_key`, `app_secret`, `expire_duration`, `expire_at`, `access_token`)
VALUES ('1', '测试', '测试使用', 'test', 'test', 'FOR_EVER', '-1', md5(uuid()));

INSERT INTO `SQLREST_CLIENT_GROUP` (`id`, `client_id`, `group_id`)
VALUES ('1', '1', '1');




