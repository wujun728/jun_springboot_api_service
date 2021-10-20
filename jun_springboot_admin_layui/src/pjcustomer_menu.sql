-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740792140611585', '客户信息' ,'54', '/index/pjCustomer','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740792140611589', '1450740792140611585', '列表' , 'pjCustomer/listByPage','pjCustomer:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792140611586', '1450740792140611585', '新增' , 'pjCustomer/add','pjCustomer:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792140611587', '1450740792140611585', '修改' , 'pjCustomer/update','pjCustomer:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740792140611588', '1450740792140611585', '删除' , 'pjCustomer/delete','pjCustomer:delete', '3',1, 1,now(),now());
