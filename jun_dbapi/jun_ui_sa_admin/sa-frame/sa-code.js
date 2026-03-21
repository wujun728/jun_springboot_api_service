// DBApi 管理平台 - 框架配置

// 设置模板标题
sa_admin.title = "DBApi";
sa_admin.logo = 'sa-frame/admin-logo.png';
sa_admin.icon = 'sa-frame/admin-logo.png';

// 自定义菜单
var myMenuList = window.menuList;
sa_admin.setMenuList(myMenuList);

// 用户信息
sa_admin.user = {
    username: localStorage.getItem('dbapi_username') || 'admin',
    avatar: 'sa-frame/admin-logo.png'
};

// 头像下拉选项
sa_admin.dropList = [
    {
        name: '修改密码',
        click: function() {
            sa_admin.showTab({id: 'db-4-1', name: '修改密码', url: 'sa-view/dbapi/setting-password.html'});
        }
    },
    {
        name: '退出登录',
        click: function() {
            sa.confirm('确定退出登录？', function() {
                if (window.dbapi) {
                    dbapi.removeToken();
                }
                localStorage.removeItem('dbapi_username');
                localStorage.removeItem('dbapi_token');
                location.href = 'login.html';
            });
        }
    }
];

// 初始化模板
sa_admin.init();
