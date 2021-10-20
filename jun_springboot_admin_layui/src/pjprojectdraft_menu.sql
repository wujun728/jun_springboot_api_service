-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740792912363521', '项目底稿' ,'54', '/index/pjProjectDraft','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740792912363525', '1450740792912363521', '列表' , 'pjProjectDraft/listByPage','pjProjectDraft:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792912363522', '1450740792912363521', '新增' , 'pjProjectDraft/add','pjProjectDraft:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792912363523', '1450740792912363521', '修改' , 'pjProjectDraft/update','pjProjectDraft:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792912363524', '1450740792912363521', '删除' , 'pjProjectDraft/delete','pjProjectDraft:delete', '3',1, 1,now(),now());
