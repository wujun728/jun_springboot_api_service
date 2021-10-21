-- 默认上级目录菜单为其他
INSERT INTO sys_permission (id, name, pid, url,target, type,order_num, deleted, status,create_time,update_time)
    VALUES ('1450740789783412737', '用户考核明细' ,'54', '/index/hrTempletQuotaDetailUserscoreDetail','_self', '2', '10',1, 1,now(),now());
-- 菜单对应按钮SQL
INSERT INTO sys_permission (id,pid, name, url, perms, type, deleted, status,create_time,update_time)
    VALUES ('1450740789783412741', '1450740789783412737', '列表' , 'hrTempletQuotaDetailUserscoreDetail/listByPage','hrTempletQuotaDetailUserscoreDetail:list', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740789783412738', '1450740789783412737', '新增' , 'hrTempletQuotaDetailUserscoreDetail/add','hrTempletQuotaDetailUserscoreDetail:add', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740789783412739', '1450740789783412737', '修改' , 'hrTempletQuotaDetailUserscoreDetail/update','hrTempletQuotaDetailUserscoreDetail:update', '3',1, 1,now(),now());
INSERT INTO sys_permission (id,pid, name, url, perms,  type, deleted, status,create_time,update_time)
    VALUES ('1450740789783412740', '1450740789783412737', '删除' , 'hrTempletQuotaDetailUserscoreDetail/delete','hrTempletQuotaDetailUserscoreDetail:delete', '3',1, 1,now(),now());
