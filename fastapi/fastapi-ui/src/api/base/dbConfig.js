import request from '@/utils/request'

// 查询数据源配置列表
export function listDbConfig(query) {
  return request({
    url: '/base/dbConfig/list',
    method: 'get',
    params: query
  })
}

// 查询数据源配置详细
export function getDbConfig(id) {
  return request({
    url: '/base/dbConfig/' + id,
    method: 'get'
  })
}

// 新增数据源配置
export function addDbConfig(data) {
  return request({
    url: '/base/dbConfig',
    method: 'post',
    data: data
  })
}

// 修改数据源配置
export function updateDbConfig(data) {
  return request({
    url: '/base/dbConfig',
    method: 'put',
    data: data
  })
}

// 删除数据源配置
export function delDbConfig(id) {
  return request({
    url: '/base/dbConfig/' + id,
    method: 'delete'
  })
}

// 查询所有数据源配置
export function findAll() {
  return request({
    url: '/base/dbConfig/findAll',
    method: 'get'
  })
}

// 一键自检
export function checkStatus() {
  return request({
    url: '/base/dbConfig/checkStatus',
    method: 'get'
  })
}
