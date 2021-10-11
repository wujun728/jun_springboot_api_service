-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1447435505186963458', '项目计划' ,'54', '/index/pjProjectPlan','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1447435505186963462', '1447435505186963458', '列表' , 'pjProjectPlan/listByPage','pjProjectPlan:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447435505186963459', '1447435505186963458', '新增' , 'pjProjectPlan/add','pjProjectPlan:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447435505186963460', '1447435505186963458', '修改' , 'pjProjectPlan/update','pjProjectPlan:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447435505186963461', '1447435505186963458', '删除' , 'pjProjectPlan/delete','pjProjectPlan:delete', '3',1, 1,now(),now());
