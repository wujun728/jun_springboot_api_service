-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status)
    VALUES ('1444643150688681985', '政策法规' ,'21', '/index/oaLawInfo','_self', '2', '10',1, 1);
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status)
    VALUES ('1444643150688681989', '1444643150688681985', '列表' , 'oaLawInfo/listByPage','oaLawInfo:list', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1444643150688681986', '1444643150688681985', '新增' , 'oaLawInfo/add','oaLawInfo:add', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1444643150688681987', '1444643150688681985', '修改' , 'oaLawInfo/update','oaLawInfo:update', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1444643150688681988', '1444643150688681985', '删除' , 'oaLawInfo/delete','oaLawInfo:delete', '3',1, 1);
