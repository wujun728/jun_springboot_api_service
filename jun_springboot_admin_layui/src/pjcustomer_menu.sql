-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1446667361711292417', '客户信息' ,'21', '/pjCustomer/list.html','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1446667361711292421', '1446667361711292417', '列表' , 'pjCustomer/listByPage','pjCustomer:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446667361711292418', '1446667361711292417', '新增' , 'pjCustomer/add','pjCustomer:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446667361711292419', '1446667361711292417', '修改' , 'pjCustomer/update','pjCustomer:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446667361711292420', '1446667361711292417', '删除' , 'pjCustomer/delete','pjCustomer:delete', '3',1, 1,now(),now());
