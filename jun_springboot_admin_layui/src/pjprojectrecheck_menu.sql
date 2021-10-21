-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740793570869250', '项目复核' ,'54', '/index/pjProjectRecheck','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740793570869254', '1450740793570869250', '列表' , 'pjProjectRecheck/listByPage','pjProjectRecheck:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740793570869251', '1450740793570869250', '新增' , 'pjProjectRecheck/add','pjProjectRecheck:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740793570869252', '1450740793570869250', '修改' , 'pjProjectRecheck/update','pjProjectRecheck:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740793570869253', '1450740793570869250', '删除' , 'pjProjectRecheck/delete','pjProjectRecheck:delete', '3',1, 1,now(),now());
