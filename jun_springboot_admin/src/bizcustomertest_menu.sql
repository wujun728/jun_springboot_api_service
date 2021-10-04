-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1445020779397496833', '客户信息' ,'21', '/index/bizCustomerTest','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1445020779397496837', '1445020779397496833', '列表' , 'bizCustomerTest/listByPage','bizCustomerTest:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1445020779397496834', '1445020779397496833', '新增' , 'bizCustomerTest/add','bizCustomerTest:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1445020779397496835', '1445020779397496833', '修改' , 'bizCustomerTest/update','bizCustomerTest:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1445020779397496836', '1445020779397496833', '删除' , 'bizCustomerTest/delete','bizCustomerTest:delete', '3',1, 1,now(),now());
