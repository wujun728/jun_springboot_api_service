-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740791750541314', '考勤' ,'54', '/index/oaUserWorktimes','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740791750541318', '1450740791750541314', '列表' , 'oaUserWorktimes/listByPage','oaUserWorktimes:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740791750541315', '1450740791750541314', '新增' , 'oaUserWorktimes/add','oaUserWorktimes:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740791750541316', '1450740791750541314', '修改' , 'oaUserWorktimes/update','oaUserWorktimes:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740791750541317', '1450740791750541314', '删除' , 'oaUserWorktimes/delete','oaUserWorktimes:delete', '3',1, 1,now(),now());
