-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1446762408259694594', '业务约定书' ,'54', '/index/pjContract','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1446762408259694598', '1446762408259694594', '列表' , 'pjContract/listByPage','pjContract:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446762408259694595', '1446762408259694594', '新增' , 'pjContract/add','pjContract:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446762408259694596', '1446762408259694594', '修改' , 'pjContract/update','pjContract:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446762408259694597', '1446762408259694594', '删除' , 'pjContract/delete','pjContract:delete', '3',1, 1,now(),now());
