-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1447474057534828546', '考勤记录' ,'54', '/index/oaPomsWorkmarksTimes','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1447474057534828550', '1447474057534828546', '列表' , 'oaPomsWorkmarksTimes/listByPage','oaPomsWorkmarksTimes:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447474057534828547', '1447474057534828546', '新增' , 'oaPomsWorkmarksTimes/add','oaPomsWorkmarksTimes:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447474057534828548', '1447474057534828546', '修改' , 'oaPomsWorkmarksTimes/update','oaPomsWorkmarksTimes:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447474057534828549', '1447474057534828546', '删除' , 'oaPomsWorkmarksTimes/delete','oaPomsWorkmarksTimes:delete', '3',1, 1,now(),now());
