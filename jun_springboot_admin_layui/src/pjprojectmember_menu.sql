-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740793176604673', '项目成员与结算' ,'54', '/index/pjProjectMember','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740793176604677', '1450740793176604673', '列表' , 'pjProjectMember/listByPage','pjProjectMember:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740793176604674', '1450740793176604673', '新增' , 'pjProjectMember/add','pjProjectMember:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740793176604675', '1450740793176604673', '修改' , 'pjProjectMember/update','pjProjectMember:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740793176604676', '1450740793176604673', '删除' , 'pjProjectMember/delete','pjProjectMember:delete', '3',1, 1,now(),now());
