import request from "../../utils/Request.js";

export function create(data : any): any {
    return request({
        url: '/app-proxy/add',
        method: 'post',
        data: data
    })
}

export function update(data : any): any {
    return request({
        url: '/app-proxy/update',
        method: 'put',
        data: data
    })
}

export function pageProxyRules(params: any): any {
    return request({
        url: '/app-proxy/page',
        method: 'get',
        params: params
    })
}

export function getAllProxyRules(params: any): any {
    return request({
        url: '/app-proxy/all',
        method: 'get',
        params: params
    })
}

export function deleteData(id : any): any {
    return request({
        url: `/app-proxy/delete?ids=` + id,
        method: 'delete'
    })
}
