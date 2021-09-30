-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status)
    VALUES ('1443505675357818881', 'bizCustomerTest' ,'21', '/index/bizCustomerTest','_self', '2', '10',1, 1);
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status)
    VALUES ('1443505675357818885', '1443505675357818881', '列表' , 'bizCustomerTest/listByPage','bizCustomerTest:list', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1443505675357818882', '1443505675357818881', '新增' , 'bizCustomerTest/add','bizCustomerTest:add', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1443505675357818883', '1443505675357818881', '修改' , 'bizCustomerTest/update','bizCustomerTest:update', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1443505675357818884', '1443505675357818881', '删除' , 'bizCustomerTest/delete','bizCustomerTest:delete', '3',1, 1);
