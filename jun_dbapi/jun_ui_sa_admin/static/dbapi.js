// =========================== DBApi AJAX 封装层 ===========================
var dbapi = {};

// API 基础路径（Nginx 反向代理模式下为空字符串，直连后端时设为 http://127.0.0.1:8520）
dbapi.baseUrl = '';

// Token 管理
dbapi.setToken = function(token) { localStorage.setItem('dbapi_token', token); };
dbapi.getToken = function() { return localStorage.getItem('dbapi_token'); };
dbapi.removeToken = function() { localStorage.removeItem('dbapi_token'); };

// 日期格式化
dbapi.formatDate = function(timestamp) {
    if (!timestamp) return '';
    var date = new Date(timestamp);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    var min = date.getMinutes();
    min = min < 10 ? ('0' + min) : min;
    var sec = date.getSeconds();
    sec = sec < 10 ? ('0' + sec) : sec;
    return y + '-' + m + '-' + d + ' ' + h + ':' + min + ':' + sec;
};

/**
 * 封装 AJAX 请求，适配后端 {msg, data, success} 格式
 * @param {string} url - 请求地址
 * @param {object|function} data - 请求参数，可省略
 * @param {function} successFn - 成功回调
 * @param {object} cfg - 额外配置 {type, contentType, processData, dataType, responseType}
 */
dbapi.ajax = function(url, data, successFn, cfg) {
    // 支持省略 data 的简写模式
    if (typeof data === 'function') {
        cfg = successFn;
        successFn = data;
        data = {};
    }
    cfg = cfg || {};
    var type = cfg.type || 'POST';
    var contentType = cfg.contentType !== undefined ? cfg.contentType : 'application/x-www-form-urlencoded; charset=UTF-8';
    var processData = cfg.processData !== undefined ? cfg.processData : true;
    var dataType = cfg.dataType || 'json';

    var ajaxOpts = {
        url: dbapi.baseUrl + url,
        type: type,
        data: data,
        dataType: dataType,
        contentType: contentType,
        processData: processData,
        beforeSend: function(xhr) {
            var token = dbapi.getToken();
            if (token) {
                xhr.setRequestHeader('Authorization', token);
            }
        },
        success: function(res) {
            // 后端 ResponseDto 格式: {msg, data, success}
            if (res && typeof res.success !== 'undefined') {
                if (res.success === true || res.success === 'true') {
                    if (successFn) successFn(res);
                } else {
                    // success=false，显示错误
                    ELEMENT.Message.error(res.msg || '操作失败');
                }
            } else {
                // 直接返回原始对象/数组的接口（无 success 字段）
                if (successFn) successFn(res);
            }
        },
        error: function(xhr) {
            if (xhr.status === 401) {
                ELEMENT.Message.warning('登录已过期，请重新登录');
                dbapi.removeToken();
                // 判断是否在 iframe 中
                if (window.parent !== window) {
                    window.parent.location.href = 'login.html';
                } else {
                    window.location.href = 'login.html';
                }
            } else if (xhr.status === 0) {
                ELEMENT.Message.error('无法连接到服务器');
            } else {
                ELEMENT.Message.error('请求失败: ' + xhr.status);
            }
        }
    };

    // 支持 blob 下载
    if (cfg.responseType === 'blob') {
        ajaxOpts.xhrFields = { responseType: 'blob' };
        ajaxOpts.dataType = undefined;
        ajaxOpts.success = function(res, status, xhr) {
            if (successFn) successFn(res, xhr);
        };
    }

    return $.ajax(ajaxOpts);
};

/**
 * 文件下载辅助方法
 * @param {Blob} blob - 文件 blob
 * @param {string} filename - 文件名
 */
dbapi.downloadBlob = function(blob, filename) {
    var link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.setAttribute('download', filename);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(link.href);
};

/**
 * 文件上传辅助方法
 * @param {string} url - 上传地址
 * @param {FormData} formData - FormData 对象
 * @param {function} successFn - 成功回调
 */
dbapi.upload = function(url, formData, successFn) {
    dbapi.ajax(url, formData, successFn, {
        contentType: false,
        processData: false
    });
};

// 数据库类型列表
dbapi.dbTypes = [
    { value: 'mysql', label: 'MySQL', icon: 'el-icon-s-platform', url: 'jdbc:mysql://localhost:3306/db?useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8' },
    { value: 'postgresql', label: 'PostgreSQL', icon: 'el-icon-s-platform', url: 'jdbc:postgresql://localhost:5432/db' },
    { value: 'sqlserver', label: 'SQL Server', icon: 'el-icon-s-platform', url: 'jdbc:microsoft:sqlserver://localhost:1433;databaseName=db' },
    { value: 'oracle', label: 'Oracle', icon: 'el-icon-s-platform', url: 'jdbc:oracle:thin:@localhost:1521:db' },
    { value: 'hive', label: 'Hive', icon: 'el-icon-s-platform', url: 'jdbc:hive2://localhost:10000/db' },
    { value: 'clickhouse', label: 'ClickHouse', icon: 'el-icon-s-platform', url: 'jdbc:clickhouse://localhost:8123/db' },
    { value: 'kylin', label: 'Kylin', icon: 'el-icon-s-platform', url: 'jdbc:kylin://localhost:7070/db' }
];

// 参数类型列表
dbapi.paramTypes = ['string', 'bigint', 'double', 'date', 'Array<string>', 'Array<bigint>', 'Array<double>'];
