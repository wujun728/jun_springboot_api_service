import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);

///////////////////////////////////////////////////////////////////////////
// 路由配置
// 参考教程：https://blog.csdn.net/weixin_38404899/article/details/90229805
//
///////////////////////////////////////////////////////////////////////////
const constantRouter = new Router({
  routes: [
    {
      path: '/',
      name: '首页',
      component: () => import('@/views/layout'),
      redirect: '/dashboard',
      children: [
        {
          path: '/dashboard',
          name: '概览',
          icon: "el-icon-menu",
          component: () => import('@/views/dashboard/index')
        },
        {
          path: '/datasource',
          name: '连接配置',
          icon: "el-icon-coin",
          component: () => import('@/views/datasource/index'),
          children: [
            {
              path: '/datasource/driver',
              name: '驱动配置',
              icon: "el-icon-help",
              component: () => import('@/views/datasource/driver'),
            },
            {
              path: '/datasource/list',
              name: '连接管理',
              icon: "el-icon-bank-card",
              component: () => import('@/views/datasource/list')
            }
          ]
        },
        {
          path: '/setting',
          name: '系统设置',
          icon: "el-icon-s-tools",
          component: () => import('@/views/setting/index'),
          children: [
            {
              path: '/setting/group',
              name: '授权分组',
              icon: "el-icon-tickets",
              component: () => import('@/views/setting/group'),
            },
            {
              path: '/setting/client',
              name: '客户应用',
              icon: "el-icon-pie-chart",
              component: () => import('@/views/setting/client')
            },
            {
              path: '/setting/firewall',
              name: '访问控制',
              icon: "el-icon-notebook-2",
              component: () => import('@/views/setting/firewall')
            },
            {
              path: '/setting/topology',
              name: '拓扑结构',
              icon: "el-icon-link",
              component: () => import('@/views/setting/topology')
            }
          ]
        },
        {
          path: '/interface',
          name: '接口管理',
          icon: "el-icon-paperclip",
          component: () => import('@/views/interface/index'),
          children: [
            {
              path: '/interface/module',
              name: '模块管理',
              icon: "el-icon-folder",
              component: () => import('@/views/interface/module'),
            },
            {
              path: '/interface/list',
              name: '接口配置',
              icon: "el-icon-refrigerator",
              component: () => import('@/views/interface/list'),
            },
            // {
            //   path: '/interface/swagger',
            //   name: '接口文档',
            //   icon: "el-icon-pie-chart",
            //   component: () => import('@/views/interface/swagger')
            // }
          ]
        },
        {
          path: '/aboutme',
          name: '关于系统',
          icon: "el-icon-s-custom",
          component: () => import('@/views/aboutme/readme')
        },
        {
          path: '/user/self',
          name: '个人中心',
          hidden: true,
          component: () => import('@/views/user/self')
        },
        {
          path: '/interface/create',
          name: '创建任务',
          hidden: true,
          component: () => import('@/views/interface/create')
        },
        {
          path: '/interface/update',
          name: '修改任务',
          hidden: true,
          component: () => import('@/views/interface/update')
        },
        {
          path: '/interface/detail',
          name: '查看任务',
          hidden: true,
          component: () => import('@/views/interface/detail')
        }
      ],
    },

    {
      path: '/login',
      name: '登录',
      component: () => import('@/views/login')
    }
  ]
});

export default constantRouter;
