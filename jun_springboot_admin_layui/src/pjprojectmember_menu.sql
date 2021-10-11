-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1447435113824808962', '项目成员与结算' ,'54', '/index/pjProjectMember','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1447435113824808966', '1447435113824808962', '列表' , 'pjProjectMember/listByPage','pjProjectMember:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447435113824808963', '1447435113824808962', '新增' , 'pjProjectMember/add','pjProjectMember:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447435113824808964', '1447435113824808962', '修改' , 'pjProjectMember/update','pjProjectMember:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1447435113824808965', '1447435113824808962', '删除' , 'pjProjectMember/delete','pjProjectMember:delete', '3',1, 1,now(),now());
