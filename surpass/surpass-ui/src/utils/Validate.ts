/**
 * 判断url是否是http或https
 * @returns {Boolean}
 * @param url
 */
export function isHttp(url: string): boolean {
    return url.indexOf('http://') !== -1 || url.indexOf('https://') !== -1
}

export function isExternal(path: any): boolean {
    if (typeof path !== 'string') {
        return false;
    }
    return /^(https?:|mailto:|tel:)/.test(path);
}