// DBApi 菜单配置
var menuList = [
    {
        id: 'db-1',
        name: '数据源管理',
        icon: 'el-icon-coin',
        info: '管理数据库连接配置',
        childList: [
            {id: 'db-1-1', name: '数据源列表', icon: 'el-icon-s-data', url: 'sa-view/dbapi/datasource-list.html'},
        ]
    },
    {
        id: 'db-2',
        name: 'API管理',
        icon: 'el-icon-connection',
        info: '管理动态 SQL API 接口',
        childList: [
            {id: 'db-2-1', name: 'API列表', icon: 'el-icon-document', url: 'sa-view/dbapi/api-list.html'},
            {id: 'db-2-2', name: 'API分组', icon: 'el-icon-folder', url: 'sa-view/dbapi/api-group.html'},
        ]
    },
    {
        id: 'db-3',
        name: '权限管理',
        icon: 'el-icon-lock',
        info: 'Token 访问控制',
        childList: [
            {id: 'db-3-1', name: 'Token管理', icon: 'el-icon-key', url: 'sa-view/dbapi/token-list.html'},
        ]
    },
    {
        id: 'db-4',
        name: '系统设置',
        icon: 'el-icon-setting',
        info: '系统参数与安全配置',
        childList: [
            {id: 'db-4-1', name: '修改密码', icon: 'el-icon-edit', url: 'sa-view/dbapi/setting-password.html'},
            {id: 'db-4-2', name: 'IP防火墙', icon: 'el-icon-warning', url: 'sa-view/dbapi/setting-firewall.html'},
        ]
    },
    // ========= 以下为原有示例菜单 =========
    {
        id: '2',
        name: '各种示例',
        icon: 'el-icon-document-remove',
        info: '增删改查各种常用组件示例',
        childList: [
            {id: '2-1', name: '查询参数示例', url: 'sa-view/case/query-p-case.html'},
            {id: '2-2', name: '表格显示示例', url: 'sa-view/case/query-table-case.html'},
            {id: '2-3', name: '表单提交示例', url: 'sa-view/case/submit-form.html'},
        ]
    },
    {
        id: '7',
        name: '系统页面',
        icon: 'el-icon-setting',
        info: '系统页面示例',
        childList: [
            {id: '7-1', name: '登录页', url: 'login.html'},
            {id: '7-8', name: '403无权限', url: 'sa-view/error-page/403.html'},
            {id: '7-9', name: '404未找到', url: 'sa-view/error-page/404.html'},
            {id: '7-10', name: '500有错误', url: 'sa-view/error-page/500.html'},
        ]
    },
];
