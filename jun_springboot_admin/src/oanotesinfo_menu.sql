-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status)
    VALUES ('1444643151024226306', '公告通知' ,'21', '/index/oaNotesInfo','_self', '2', '10',1, 1);
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status)
    VALUES ('1444643151024226310', '1444643151024226306', '列表' , 'oaNotesInfo/listByPage','oaNotesInfo:list', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1444643151024226307', '1444643151024226306', '新增' , 'oaNotesInfo/add','oaNotesInfo:add', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1444643151024226308', '1444643151024226306', '修改' , 'oaNotesInfo/update','oaNotesInfo:update', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1444643151024226309', '1444643151024226306', '删除' , 'oaNotesInfo/delete','oaNotesInfo:delete', '3',1, 1);
