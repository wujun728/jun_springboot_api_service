-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1446768955891175425', '项目报告文号' ,'54', '/index/pjProjectReportnumber','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1446768955891175429', '1446768955891175425', '列表' , 'pjProjectReportnumber/listByPage','pjProjectReportnumber:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446768955891175426', '1446768955891175425', '新增' , 'pjProjectReportnumber/add','pjProjectReportnumber:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446768955891175427', '1446768955891175425', '修改' , 'pjProjectReportnumber/update','pjProjectReportnumber:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446768955891175428', '1446768955891175425', '删除' , 'pjProjectReportnumber/delete','pjProjectReportnumber:delete', '3',1, 1,now(),now());
