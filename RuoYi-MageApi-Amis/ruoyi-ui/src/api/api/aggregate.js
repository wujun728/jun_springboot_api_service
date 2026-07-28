import request from '@/utils/request'
export function listAggregate(query) { return request({ url: '/api/aggregate/list', method: 'get', params: query }) }
export function getAggregate(id) { return request({ url: '/api/aggregate/' + id, method: 'get' }) }
export function addAggregate(data) { return request({ url: '/api/aggregate', method: 'post', data }) }
export function updateAggregate(data) { return request({ url: '/api/aggregate', method: 'put', data }) }
export function delAggregate(id) { return request({ url: '/api/aggregate/' + id, method: 'delete' }) }