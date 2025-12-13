import { createRouter, createWebHistory } from 'vue-router'
import AllMatchesPage from '../views/AllMatchesPage.vue'
import FavoriteMatchesPage from '../views/FavoriteMatchesPage.vue'

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
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router