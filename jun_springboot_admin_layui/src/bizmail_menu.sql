-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740788592230402', '短信&消息&邮件' ,'54', '/index/bizMail','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740788592230406', '1450740788592230402', '列表' , 'bizMail/listByPage','bizMail:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740788592230403', '1450740788592230402', '新增' , 'bizMail/add','bizMail:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740788592230404', '1450740788592230402', '修改' , 'bizMail/update','bizMail:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740788592230405', '1450740788592230402', '删除' , 'bizMail/delete','bizMail:delete', '3',1, 1,now(),now());
