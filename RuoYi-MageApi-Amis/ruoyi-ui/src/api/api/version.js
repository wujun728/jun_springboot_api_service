import request from '@/utils/request'
export function listVersion(query) { return request({ url: '/api/version/list', method: 'get', params: query }) }
export function getVersion(id) { return request({ url: '/api/version/detail/' + id, method: 'get' }) }
export function rollbackVersion(apiId, versionNo) { return request({ url: '/api/version/rollback/' + apiId + '/' + versionNo, method: 'post' }) }