-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status)
    VALUES ('1444323471227850753', '公告通知' ,'21', '/index/oaNotesInfo','_self', '2', '10',1, 1);
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status)
    VALUES ('1444323471227850757', '1444323471227850753', '列表' , 'oaNotesInfo/listByPage','oaNotesInfo:list', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1444323471227850754', '1444323471227850753', '新增' , 'oaNotesInfo/add','oaNotesInfo:add', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1444323471227850755', '1444323471227850753', '修改' , 'oaNotesInfo/update','oaNotesInfo:update', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1444323471227850756', '1444323471227850753', '删除' , 'oaNotesInfo/delete','oaNotesInfo:delete', '3',1, 1);
