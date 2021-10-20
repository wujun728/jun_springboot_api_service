-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740790806822914', '项目总结及评价' ,'54', '/index/oaOfficeCount','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740790806822918', '1450740790806822914', '列表' , 'oaOfficeCount/listByPage','oaOfficeCount:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740790806822915', '1450740790806822914', '新增' , 'oaOfficeCount/add','oaOfficeCount:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740790806822916', '1450740790806822914', '修改' , 'oaOfficeCount/update','oaOfficeCount:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740790806822917', '1450740790806822914', '删除' , 'oaOfficeCount/delete','oaOfficeCount:delete', '3',1, 1,now(),now());
