import request from '@/utils/request'

// 查询应用管理列表
export function listAppManager(query) {
  return request({
    url: '/base/appManager/list',
    method: 'get',
    params: query
  })
}

// 查询应用管理详细
export function getAppManager(id) {
  return request({
    url: '/base/appManager/' + id,
    method: 'get'
  })
}

// 新增应用管理
export function addAppManager(data) {
  return request({
    url: '/base/appManager',
    method: 'post',
    data: data
  })
}

// 修改应用管理
export function updateAppManager(data) {
  return request({
    url: '/base/appManager',
    method: 'put',
    data: data
  })
}

// 删除应用管理
export function delAppManager(id) {
  return request({
    url: '/base/appManager/' + id,
    method: 'delete'
  })
}

// 查询所有应用管理
export function findAll() {
  return request({
    url: '/base/appManager/findAll',
    method: 'get'
  })
}
