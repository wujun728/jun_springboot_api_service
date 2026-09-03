import request from '@/utils/request'

// 查询应用授权列表
export function listIntfaceAppAuth(query) {
  return request({
    url: '/iservice/intfaceAppAuth/list',
    method: 'get',
    params: query
  })
}

// 新增接口应用授权
export function addIntfaceAppAuth(data) {
  return request({
    url: '/iservice/intfaceAppAuth',
    method: 'post',
    data: data
  })
}

// 取消授权
export function cancleAuth(data) {
  return request({
    url: '/iservice/intfaceAppAuth/cancleAuth',
    method: 'post',
    data: data
  })
}
