-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status)
    VALUES ('1444643150957117441', '培训学习' ,'21', '/index/oaLearnInfo','_self', '2', '10',1, 1);
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status)
    VALUES ('1444643150957117445', '1444643150957117441', '列表' , 'oaLearnInfo/listByPage','oaLearnInfo:list', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1444643150957117442', '1444643150957117441', '新增' , 'oaLearnInfo/add','oaLearnInfo:add', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1444643150957117443', '1444643150957117441', '修改' , 'oaLearnInfo/update','oaLearnInfo:update', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1444643150957117444', '1444643150957117441', '删除' , 'oaLearnInfo/delete','oaLearnInfo:delete', '3',1, 1);
