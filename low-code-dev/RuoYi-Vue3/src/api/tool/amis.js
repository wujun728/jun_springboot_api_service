import request from '@/utils/request'

// http://10.10.12.114:8909/amis/list?page=1&id=1
// 查询生成表数据
export function getId(id) {
  return request({
    url: '/mapi/amis/list?id='+id,
    method: 'get',
  })
}
