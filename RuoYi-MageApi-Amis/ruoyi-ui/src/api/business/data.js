import request from '@/utils/request'

// 查询测试数据列表
export function listData(query) {
  return request({
    //url:'/test/test',
    url: '/business/data/list',
    method: 'post',
    data: query
  })
}

// 查询测试数据详细
export function getData(id) {
  return request({
    url: '/business/data/' + id,
    method: 'get'
  })
}

// 新增测试数据
export function addData(data) {
  return request({
    url: '/business/data',
    method: 'post',
    data: data
  })
}

// 修改测试数据
export function updateData(data) {
  return request({
    url: '/business/data',
    method: 'put',
    data: data
  })
}

// 删除测试数据
export function delData(id) {
  return request({
    url: '/business/data/' + id,
    method: 'delete'
  })
}
