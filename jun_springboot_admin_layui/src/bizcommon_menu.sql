-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740788021805057', '公共信息' ,'54', '/index/bizCommon','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740788021805061', '1450740788021805057', '列表' , 'bizCommon/listByPage','bizCommon:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740788021805058', '1450740788021805057', '新增' , 'bizCommon/add','bizCommon:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740788021805059', '1450740788021805057', '修改' , 'bizCommon/update','bizCommon:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740788021805060', '1450740788021805057', '删除' , 'bizCommon/delete','bizCommon:delete', '3',1, 1,now(),now());
