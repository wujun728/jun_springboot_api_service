import request from '@/utils/request'

// 查询接口应用申请列表
export function listIntfaceAppApplyFor(query) {
  return request({
    url: '/iservice/intfaceAppApplyFor/list',
    method: 'get',
    params: query
  })
}

// 查询接口应用申请详细
export function getIntfaceAppApplyFor(id) {
  return request({
    url: '/iservice/intfaceAppApplyFor/' + id,
    method: 'get'
  })
}

// 新增接口应用申请
export function addIntfaceAppApplyFor(data) {
  return request({
    url: '/iservice/intfaceAppApplyFor',
    method: 'post',
    data: data
  })
}

// 修改接口应用申请
export function updateIntfaceAppApplyFor(data) {
  return request({
    url: '/iservice/intfaceAppApplyFor',
    method: 'put',
    data: data
  })
}

// 删除接口应用申请
export function delIntfaceAppApplyFor(id) {
  return request({
    url: '/iservice/intfaceAppApplyFor/' + id,
    method: 'delete'
  })
}

// 查询密钥
export function selectSecret(id) {
  return request({
    url: '/iservice/intfaceAppApplyFor/selectSecret/' + id,
    method: 'get'
  })
}

// 重置密钥
export function resetSecret(id) {
  return request({
    url: '/iservice/intfaceAppApplyFor/resetSecret/' + id,
    method: 'get'
  })
}
