-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740789363982337', '用户考核记录' ,'54', '/index/hrTempletAssessmentUserscore','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740789363982341', '1450740789363982337', '列表' , 'hrTempletAssessmentUserscore/listByPage','hrTempletAssessmentUserscore:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740789363982338', '1450740789363982337', '新增' , 'hrTempletAssessmentUserscore/add','hrTempletAssessmentUserscore:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740789363982339', '1450740789363982337', '修改' , 'hrTempletAssessmentUserscore/update','hrTempletAssessmentUserscore:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740789363982340', '1450740789363982337', '删除' , 'hrTempletAssessmentUserscore/delete','hrTempletAssessmentUserscore:delete', '3',1, 1,now(),now());
