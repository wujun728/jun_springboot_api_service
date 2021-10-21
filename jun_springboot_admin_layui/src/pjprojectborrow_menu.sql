-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740792711036930', '项目借阅' ,'54', '/index/pjProjectBorrow','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740792711036934', '1450740792711036930', '列表' , 'pjProjectBorrow/listByPage','pjProjectBorrow:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792711036931', '1450740792711036930', '新增' , 'pjProjectBorrow/add','pjProjectBorrow:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792711036932', '1450740792711036930', '修改' , 'pjProjectBorrow/update','pjProjectBorrow:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792711036933', '1450740792711036930', '删除' , 'pjProjectBorrow/delete','pjProjectBorrow:delete', '3',1, 1,now(),now());
