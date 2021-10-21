-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740792841060353', '项目日报周报' ,'54', '/index/pjProjectDaily','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740792841060357', '1450740792841060353', '列表' , 'pjProjectDaily/listByPage','pjProjectDaily:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792841060354', '1450740792841060353', '新增' , 'pjProjectDaily/add','pjProjectDaily:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792841060355', '1450740792841060353', '修改' , 'pjProjectDaily/update','pjProjectDaily:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792841060356', '1450740792841060353', '删除' , 'pjProjectDaily/delete','pjProjectDaily:delete', '3',1, 1,now(),now());
