import request from '@/utils/request'

// 查询查询接口管理列表
export function listSelectIntfaceManager(query) {
  return request({
    url: '/imanager/selectIntfaceManager/list',
    method: 'get',
    params: query
  })
}

// 查询查询接口管理详细
export function getSelectIntfaceManager(id) {
  return request({
    url: '/imanager/selectIntfaceManager/' + id,
    method: 'get'
  })
}

// 新增查询接口管理
export function addSelectIntfaceManager(data) {
  return request({
    url: '/imanager/selectIntfaceManager',
    method: 'post',
    data: data
  })
}

// 修改查询接口管理
export function updateSelectIntfaceManager(data) {
  return request({
    url: '/imanager/selectIntfaceManager',
    method: 'put',
    data: data
  })
}

// 删除查询接口管理
export function delSelectIntfaceManager(id) {
  return request({
    url: '/imanager/selectIntfaceManager/' + id,
    method: 'delete'
  })
}

// 修改转换信息
export function updateConverInfo(data) {
  return request({
    url: '/imanager/selectIntfaceManager/updateConverInfo',
    method: 'put',
    data: data
  })
}

