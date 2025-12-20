<template>
  <div id="app">
    <div v-if="currentUser" class="app-container">
      <div style="text-align: center; margin-bottom: 30px;">
        <h1>TennisFan</h1>
        <p style="color: #666;">Сервис для теннисных фанатов</p>
      </div>
      
      <div style="display: flex; justify-content: center; gap: 20px; margin-bottom: 30px; flex-wrap: wrap;">
        <router-link 
          to="/favorite-matches" 
          :class="['nav-btn', { active: $route.name === 'FavoriteMatches' }]"
        >
          Избранные игроки
        </router-link>
        
        <router-link 
          to="/all-matches" 
          :class="['nav-btn', { active: $route.name === 'AllMatches' }]"
        >
          Все матчи
        </router-link>
        
        <router-link 
          v-if="currentUser.role === 'ADMIN'"
          to="/admin" 
          :class="['nav-btn-admin', { active: $route.name === 'AdminPanel' }]"
        >
          Панель администратора
        </router-link>
        
        <button @click="logout" class="logout-btn-small">
          Выйти
        </button>
      </div>
      
      <div style="text-align: center; margin-bottom: 20px; color: #666;">
        Пользователь: <strong>{{ currentUser.username }}</strong>
        <span v-if="currentUser.role === 'ADMIN'" style="color: #4CAF50; margin-left: 10px;">
          (Администратор)
        </span>
      </div>
      
      <main class="main-content">
        <router-view :userId="currentUser.id" />
      </main>
    </div>
    
    <div v-else class="auth-container-old">
      <h1>TennisFan</h1>
      <p style="color: #666; margin-bottom: 30px;">Сервис для теннисных фанатов</p>
      
      <LoginForm @login-success="handleLoginSuccess" />
      
      <div v-if="message" :style="{ 
        color: messageType === 'error' ? 'red' : 'green',
        marginTop: '20px',
        textAlign: 'center'
      }">
        {{ message }}
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import LoginForm from './components/LoginForm.vue'

export default {
  name: 'App',
  components: {
    LoginForm
  },
  
  setup() {
    const currentUser = ref(null)
    const message = ref('')
    const messageType = ref('')
    const router = useRouter()
    
    onMounted(() => {
      const savedUser = localStorage.getItem('tennisfan_user')
      if (savedUser) {
        currentUser.value = JSON.parse(savedUser)
        if (router.currentRoute.value.path === '/') {
          router.push('/favorite-matches')
        }
      }
    })
    
    const handleLoginSuccess = (userData) => {
      currentUser.value = userData
      localStorage.setItem('tennisfan_user', JSON.stringify(userData))
      showMessage('Успешный вход!', 'success')
      router.push('/favorite-matches')
    }
    
    const logout = () => {
      currentUser.value = null
      localStorage.removeItem('tennisfan_user')
      router.push('/')
    }
    
    const showMessage = (text, type) => {
      message.value = text
      messageType.value = type
      setTimeout(() => {
        message.value = ''
        messageType.value = ''
      }, 3000)
    }
    
    return {
      currentUser,
      message,
      messageType,
      handleLoginSuccess,
      logout,
      showMessage
    }
  }
}
</script>

<style>
#app {
  font-family: Arial, sans-serif;
  max-width: 800px;
  margin: 50px auto;
  padding: 20px;
}

.auth-container-old {
  text-align: center;
}

.nav-btn {
  display: inline-block;
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  border: none;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.nav-btn:hover {
  background-color: #45a049;
  text-decoration: none;
}

.nav-btn.active {
  background-color: #2E7D32;
}

.nav-btn-admin {
  display: inline-block;
  padding: 10px 20px;
  background-color: rgba(33, 150, 243, 0.1);
  color: #2196F3;
  text-decoration: none;
  border-radius: 4px;
  border: 1px solid rgba(33, 150, 243, 0.3);
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.nav-btn-admin:hover {
  background-color: rgba(33, 150, 243, 0.2);
  text-decoration: none;
}

.nav-btn-admin.active {
  background-color: rgba(33, 150, 243, 0.3);
  border-color: #2196F3;
}

.logout-btn-small {
  padding: 10px 20px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.logout-btn-small:hover {
  background-color: #d32f2f;
}

.main-content {
  margin-top: 20px;
}
</style>