-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740791930896385', '业务约定书' ,'54', '/index/pjContract','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740791930896389', '1450740791930896385', '列表' , 'pjContract/listByPage','pjContract:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740791930896386', '1450740791930896385', '新增' , 'pjContract/add','pjContract:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740791930896387', '1450740791930896385', '修改' , 'pjContract/update','pjContract:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740791930896388', '1450740791930896385', '删除' , 'pjContract/delete','pjContract:delete', '3',1, 1,now(),now());
