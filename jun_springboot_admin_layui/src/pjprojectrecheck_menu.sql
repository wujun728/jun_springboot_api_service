-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1447435921647747074', '项目复核' ,'54', '/index/pjProjectRecheck','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1447435921647747078', '1447435921647747074', '列表' , 'pjProjectRecheck/listByPage','pjProjectRecheck:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447435921647747075', '1447435921647747074', '新增' , 'pjProjectRecheck/add','pjProjectRecheck:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447435921647747076', '1447435921647747074', '修改' , 'pjProjectRecheck/update','pjProjectRecheck:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447435921647747077', '1447435921647747074', '删除' , 'pjProjectRecheck/delete','pjProjectRecheck:delete', '3',1, 1,now(),now());
