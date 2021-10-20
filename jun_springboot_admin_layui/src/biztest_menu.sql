-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740788948746241', '客户信息' ,'54', '/index/bizTest','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740788948746245', '1450740788948746241', '列表' , 'bizTest/listByPage','bizTest:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740788948746242', '1450740788948746241', '新增' , 'bizTest/add','bizTest:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740788948746243', '1450740788948746241', '修改' , 'bizTest/update','bizTest:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740788948746244', '1450740788948746241', '删除' , 'bizTest/delete','bizTest:delete', '3',1, 1,now(),now());
