import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '@/components/LoginView.vue';
import ShoppingPage from '@/components/ShoppingPage.vue';
import ShoppingCart from '@/components/ShoppingCart.vue';
import AdminDashboard from '@/components/AdminDashboard.vue';
import LogbookPage from '@/components/LogbookPage.vue';

const routes = [
  {
    path: '/',
    name: 'Login',
    component: LoginView
  },  
  {
    path: '/shopping',
    name: 'Shopping',
    component: ShoppingPage
  },
  {
    path: '/cart',
    name: 'ShoppingCart',
    component: ShoppingCart
  },
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: AdminDashboard
  },
  {
    path: '/logbook',
    name: 'LogbookPage',
    component: LogbookPage
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;