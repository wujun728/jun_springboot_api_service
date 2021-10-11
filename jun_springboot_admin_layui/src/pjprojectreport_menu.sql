-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1446766107145105409', '项目报告' ,'54', '/index/pjProjectReport','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1446766107145105413', '1446766107145105409', '列表' , 'pjProjectReport/listByPage','pjProjectReport:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446766107145105410', '1446766107145105409', '新增' , 'pjProjectReport/add','pjProjectReport:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446766107145105411', '1446766107145105409', '修改' , 'pjProjectReport/update','pjProjectReport:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446766107145105412', '1446766107145105409', '删除' , 'pjProjectReport/delete','pjProjectReport:delete', '3',1, 1,now(),now());
