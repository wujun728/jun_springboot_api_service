-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1447474056997957633', '外出信息' ,'54', '/index/oaPomsWorkmarksOutsite','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1447474056997957637', '1447474056997957633', '列表' , 'oaPomsWorkmarksOutsite/listByPage','oaPomsWorkmarksOutsite:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447474056997957634', '1447474056997957633', '新增' , 'oaPomsWorkmarksOutsite/add','oaPomsWorkmarksOutsite:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447474056997957635', '1447474056997957633', '修改' , 'oaPomsWorkmarksOutsite/update','oaPomsWorkmarksOutsite:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447474056997957636', '1447474056997957633', '删除' , 'oaPomsWorkmarksOutsite/delete','oaPomsWorkmarksOutsite:delete', '3',1, 1,now(),now());
