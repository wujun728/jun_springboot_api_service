import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/designer',
      name: 'form-designer',
      component: () => import('../views/FormDesigner.vue'),
    },
    {
      path: '/preview/:id?',
      name: 'form-preview',
      component: () => import('../views/FormPreview.vue'),
    },
    {
      path: '/list-designer',
      name: 'list-designer',
      component: () => import('../views/ListDesigner.vue'),
    },
    {
      path: '/list-preview/:id?',
      name: 'list-preview',
      component: () => import('../views/ListPreview.vue'),
    },
    {
      path: '/workflow-designer',
      name: 'workflow-designer',
      component: () => import('../views/WorkflowDesigner.vue'),
    },
    {
      path: '/workflow-preview/:id?',
      name: 'workflow-preview',
      component: () => import('../views/WorkflowPreview.vue'),
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'),
    }
  ],
})

export default router
