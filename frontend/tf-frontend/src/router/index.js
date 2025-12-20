import { createRouter, createWebHistory } from 'vue-router'
import AllMatchesPage from '../views/AllMatchesPage.vue'
import FavoriteMatchesPage from '../views/FavoriteMatchesPage.vue'
import AdminPanel from '../views/AdminPanel.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    redirect: '/favorite-matches'
  },
  {
    path: '/all-matches',
    name: 'AllMatches',
    component: AllMatchesPage
  },
  {
    path: '/favorite-matches',
    name: 'FavoriteMatches',
    component: FavoriteMatchesPage
  },
  {
    path: '/admin',
    name: 'AdminPanel',
    component: AdminPanel
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router