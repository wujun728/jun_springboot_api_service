-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740791465328641', '员工请假' ,'54', '/index/oaUserLeave','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740791465328645', '1450740791465328641', '列表' , 'oaUserLeave/listByPage','oaUserLeave:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740791465328642', '1450740791465328641', '新增' , 'oaUserLeave/add','oaUserLeave:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740791465328643', '1450740791465328641', '修改' , 'oaUserLeave/update','oaUserLeave:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740791465328644', '1450740791465328641', '删除' , 'oaUserLeave/delete','oaUserLeave:delete', '3',1, 1,now(),now());
