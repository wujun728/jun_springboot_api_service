-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status)
    VALUES ('1441746003245633538', 'bizCustomer' ,'21', '/index/bizCustomer','_self', '2', '10',1, 1);
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status)
    VALUES ('1441746003245633542', '1441746003245633538', '列表' , 'bizCustomer/listByPage','bizCustomer:list', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1441746003245633539', '1441746003245633538', '新增' , 'bizCustomer/add','bizCustomer:add', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1441746003245633540', '1441746003245633538', '修改' , 'bizCustomer/update','bizCustomer:update', '3',1, 1);
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status)
    VALUES ('1441746003245633541', '1441746003245633538', '删除' , 'bizCustomer/delete','bizCustomer:delete', '3',1, 1);
