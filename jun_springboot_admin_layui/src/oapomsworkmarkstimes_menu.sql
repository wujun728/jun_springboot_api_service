-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740791322722305', '考勤记录' ,'54', '/index/oaPomsWorkmarksTimes','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740791322722309', '1450740791322722305', '列表' , 'oaPomsWorkmarksTimes/listByPage','oaPomsWorkmarksTimes:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740791322722306', '1450740791322722305', '新增' , 'oaPomsWorkmarksTimes/add','oaPomsWorkmarksTimes:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740791322722307', '1450740791322722305', '修改' , 'oaPomsWorkmarksTimes/update','oaPomsWorkmarksTimes:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740791322722308', '1450740791322722305', '删除' , 'oaPomsWorkmarksTimes/delete','oaPomsWorkmarksTimes:delete', '3',1, 1,now(),now());
