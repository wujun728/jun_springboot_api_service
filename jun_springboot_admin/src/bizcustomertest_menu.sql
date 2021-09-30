-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status)
    VALUES ('1443470660070748161', 'bizCustomerTest' ,'21', '/index/bizCustomerTest','_self', '2', '10',1, 1);
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status)
    VALUES ('1443470660070748165', '1443470660070748161', '列表' , 'bizCustomerTest/listByPage','bizCustomerTest:list', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1443470660070748162', '1443470660070748161', '新增' , 'bizCustomerTest/add','bizCustomerTest:add', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1443470660070748163', '1443470660070748161', '修改' , 'bizCustomerTest/update','bizCustomerTest:update', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1443470660070748164', '1443470660070748161', '删除' , 'bizCustomerTest/delete','bizCustomerTest:delete', '3',1, 1);
