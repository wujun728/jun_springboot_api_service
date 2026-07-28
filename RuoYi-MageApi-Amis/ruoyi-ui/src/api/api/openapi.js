import request from '@/utils/request'
export function listOpenApiApp(query) { return request({ url: '/api/openapi/app/list', method: 'get', params: query }) }
export function getOpenApiApp(id) { return request({ url: '/api/openapi/app/' + id, method: 'get' }) }
export function addOpenApiApp(data) { return request({ url: '/api/openapi/app', method: 'post', data }) }
export function updateOpenApiApp(data) { return request({ url: '/api/openapi/app', method: 'put', data }) }
export function delOpenApiApp(id) { return request({ url: '/api/openapi/app/' + id, method: 'delete' }) }