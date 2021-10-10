-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1446738573166645249', '公共信息' ,'54', '/index/bizCommon','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1446738573166645253', '1446738573166645249', '列表' , 'bizCommon/listByPage','bizCommon:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446738573166645250', '1446738573166645249', '新增' , 'bizCommon/add','bizCommon:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446738573166645251', '1446738573166645249', '修改' , 'bizCommon/update','bizCommon:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1446738573166645252', '1446738573166645249', '删除' , 'bizCommon/delete','bizCommon:delete', '3',1, 1,now(),now());
