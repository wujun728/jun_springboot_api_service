-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740790995566593', '外出信息' ,'54', '/index/oaPomsWorkmarksOutsite','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740790995566597', '1450740790995566593', '列表' , 'oaPomsWorkmarksOutsite/listByPage','oaPomsWorkmarksOutsite:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740790995566594', '1450740790995566593', '新增' , 'oaPomsWorkmarksOutsite/add','oaPomsWorkmarksOutsite:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740790995566595', '1450740790995566593', '修改' , 'oaPomsWorkmarksOutsite/update','oaPomsWorkmarksOutsite:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740790995566596', '1450740790995566593', '删除' , 'oaPomsWorkmarksOutsite/delete','oaPomsWorkmarksOutsite:delete', '3',1, 1,now(),now());
