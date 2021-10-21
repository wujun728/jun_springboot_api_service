-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740793952550914', '项目报告文号' ,'54', '/index/pjProjectReportnumber','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740793952550918', '1450740793952550914', '列表' , 'pjProjectReportnumber/listByPage','pjProjectReportnumber:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740793952550915', '1450740793952550914', '新增' , 'pjProjectReportnumber/add','pjProjectReportnumber:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740793952550916', '1450740793952550914', '修改' , 'pjProjectReportnumber/update','pjProjectReportnumber:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740793952550917', '1450740793952550914', '删除' , 'pjProjectReportnumber/delete','pjProjectReportnumber:delete', '3',1, 1,now(),now());
