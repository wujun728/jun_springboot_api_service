-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1444929789366075394', '客户信息' ,'21', '/index/bizCustomerTest','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1444929789366075398', '1444929789366075394', '列表' , 'bizCustomerTest/listByPage','bizCustomerTest:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1444929789366075395', '1444929789366075394', '新增' , 'bizCustomerTest/add','bizCustomerTest:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1444929789366075396', '1444929789366075394', '修改' , 'bizCustomerTest/update','bizCustomerTest:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1444929789366075397', '1444929789366075394', '删除' , 'bizCustomerTest/delete','bizCustomerTest:delete', '3',1, 1,now(),now());
