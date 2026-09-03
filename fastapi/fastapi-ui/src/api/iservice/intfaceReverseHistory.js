import request from '@/utils/request'

// 查询接口调用历史列表
export function listIntfaceReverseHistory(query) {
  return request({
    url: '/iservice/intfaceReverseHistory/list',
    method: 'get',
    params: query
  })
}

// 查询接口调用历史详细
export function getIntfaceReverseHistory(id) {
  return request({
    url: '/iservice/intfaceReverseHistory/' + id,
    method: 'get'
  })
}
