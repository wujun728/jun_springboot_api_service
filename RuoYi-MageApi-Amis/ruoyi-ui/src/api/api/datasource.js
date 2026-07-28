import request from '@/utils/request'
export function listDataSource(query) { return request({ url: '/api/datasource/list', method: 'get', params: query }) }
export function getDataSource(id) { return request({ url: '/api/datasource/' + id, method: 'get' }) }
export function addDataSource(data) { return request({ url: '/api/datasource', method: 'post', data }) }
export function updateDataSource(data) { return request({ url: '/api/datasource', method: 'put', data }) }
export function delDataSource(ids) { return request({ url: '/api/datasource/' + ids, method: 'delete' }) }