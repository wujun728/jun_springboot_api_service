import request from '@/utils/request'
export function listPlugins(type) { return request({ url: '/api/plugin/list', method: 'get', params: { type } }) }