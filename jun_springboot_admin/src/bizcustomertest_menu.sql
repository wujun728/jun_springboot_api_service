-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status)
    VALUES ('1443109218637103106', 'bizCustomerTest' ,'21', '/index/bizCustomerTest','_self', '2', '10',1, 1);
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status)
    VALUES ('1443109218637103110', '1443109218637103106', '列表' , 'bizCustomerTest/listByPage','bizCustomerTest:list', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1443109218637103107', '1443109218637103106', '新增' , 'bizCustomerTest/add','bizCustomerTest:add', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1443109218637103108', '1443109218637103106', '修改' , 'bizCustomerTest/update','bizCustomerTest:update', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1443109218637103109', '1443109218637103106', '删除' , 'bizCustomerTest/delete','bizCustomerTest:delete', '3',1, 1);
