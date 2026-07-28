import request from '@/utils/request'
export function listGroovyApi(query) { return request({ url: '/api/groovy/list', method: 'get', params: query }) }
export function getGroovyApi(id) { return request({ url: '/api/groovy/' + id, method: 'get' }) }
export function addGroovyApi(data) { return request({ url: '/api/groovy', method: 'post', data }) }
export function updateGroovyApi(data) { return request({ url: '/api/groovy', method: 'put', data }) }
export function delGroovyApi(id) { return request({ url: '/api/groovy/' + id, method: 'delete' }) }
export function testGroovyApi(id) { return request({ url: '/api/groovy/test/' + id, method: 'get' }) }