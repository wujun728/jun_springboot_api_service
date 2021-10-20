-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740793763807233', '项目报告' ,'54', '/index/pjProjectReport','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740793763807237', '1450740793763807233', '列表' , 'pjProjectReport/listByPage','pjProjectReport:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740793763807234', '1450740793763807233', '新增' , 'pjProjectReport/add','pjProjectReport:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740793763807235', '1450740793763807233', '修改' , 'pjProjectReport/update','pjProjectReport:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740793763807236', '1450740793763807233', '删除' , 'pjProjectReport/delete','pjProjectReport:delete', '3',1, 1,now(),now());
