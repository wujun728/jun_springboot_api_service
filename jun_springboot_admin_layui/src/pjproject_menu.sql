-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740792304189442', '项目信息' ,'54', '/index/pjProject','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740792304189446', '1450740792304189442', '列表' , 'pjProject/listByPage','pjProject:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792304189443', '1450740792304189442', '新增' , 'pjProject/add','pjProject:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792304189444', '1450740792304189442', '修改' , 'pjProject/update','pjProject:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792304189445', '1450740792304189442', '删除' , 'pjProject/delete','pjProject:delete', '3',1, 1,now(),now());
