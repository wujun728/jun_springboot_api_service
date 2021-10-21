-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740789124907009', '考核模板' ,'54', '/index/hrTempletAssessment','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740789124907013', '1450740789124907009', '列表' , 'hrTempletAssessment/listByPage','hrTempletAssessment:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740789124907010', '1450740789124907009', '新增' , 'hrTempletAssessment/add','hrTempletAssessment:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740789124907011', '1450740789124907009', '修改' , 'hrTempletAssessment/update','hrTempletAssessment:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740789124907012', '1450740789124907009', '删除' , 'hrTempletAssessment/delete','hrTempletAssessment:delete', '3',1, 1,now(),now());
