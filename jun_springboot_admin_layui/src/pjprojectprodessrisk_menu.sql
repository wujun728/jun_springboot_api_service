-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740793466011650', '项目进度与风险' ,'54', '/index/pjProjectProdessRisk','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740793466011654', '1450740793466011650', '列表' , 'pjProjectProdessRisk/listByPage','pjProjectProdessRisk:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740793466011651', '1450740793466011650', '新增' , 'pjProjectProdessRisk/add','pjProjectProdessRisk:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740793466011652', '1450740793466011650', '修改' , 'pjProjectProdessRisk/update','pjProjectProdessRisk:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740793466011653', '1450740793466011650', '删除' , 'pjProjectProdessRisk/delete','pjProjectProdessRisk:delete', '3',1, 1,now(),now());
