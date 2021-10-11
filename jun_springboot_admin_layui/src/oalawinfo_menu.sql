-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1447466101493751810', '政策法规' ,'54', '/index/oaLawInfo','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1447466101493751814', '1447466101493751810', '列表' , 'oaLawInfo/listByPage','oaLawInfo:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447466101493751811', '1447466101493751810', '新增' , 'oaLawInfo/add','oaLawInfo:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447466101493751812', '1447466101493751810', '修改' , 'oaLawInfo/update','oaLawInfo:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447466101493751813', '1447466101493751810', '删除' , 'oaLawInfo/delete','oaLawInfo:delete', '3',1, 1,now(),now());
