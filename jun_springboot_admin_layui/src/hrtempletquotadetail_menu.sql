-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740789527560194', '考核模板明细' ,'54', '/index/hrTempletQuotaDetail','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740789527560198', '1450740789527560194', '列表' , 'hrTempletQuotaDetail/listByPage','hrTempletQuotaDetail:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740789527560195', '1450740789527560194', '新增' , 'hrTempletQuotaDetail/add','hrTempletQuotaDetail:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740789527560196', '1450740789527560194', '修改' , 'hrTempletQuotaDetail/update','hrTempletQuotaDetail:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740789527560197', '1450740789527560194', '删除' , 'hrTempletQuotaDetail/delete','hrTempletQuotaDetail:delete', '3',1, 1,now(),now());
