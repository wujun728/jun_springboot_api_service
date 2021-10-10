-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1446743678683238401', '项目信息' ,'54', '/index/pjProject','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1446743678683238405', '1446743678683238401', '列表' , 'pjProject/listByPage','pjProject:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446743678683238402', '1446743678683238401', '新增' , 'pjProject/add','pjProject:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446743678683238403', '1446743678683238401', '修改' , 'pjProject/update','pjProject:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446743678683238404', '1446743678683238401', '删除' , 'pjProject/delete','pjProject:delete', '3',1, 1,now(),now());
