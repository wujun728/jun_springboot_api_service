-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740792501321729', '项目总结及评价' ,'54', '/index/pjProjectAppraise','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740792501321733', '1450740792501321729', '列表' , 'pjProjectAppraise/listByPage','pjProjectAppraise:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792501321730', '1450740792501321729', '新增' , 'pjProjectAppraise/add','pjProjectAppraise:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792501321731', '1450740792501321729', '修改' , 'pjProjectAppraise/update','pjProjectAppraise:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792501321732', '1450740792501321729', '删除' , 'pjProjectAppraise/delete','pjProjectAppraise:delete', '3',1, 1,now(),now());
