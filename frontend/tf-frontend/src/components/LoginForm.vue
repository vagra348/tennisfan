<template>
  <div class="login-form-old">
    <h2>Вход / Регистрация</h2>
    
    <div>
      <input 
        v-model="username" 
        placeholder="Логин" 
        @keyup.enter="login"
        style="margin: 5px; padding: 8px; font-size: 16px; width: 200px;"
      />
    </div>
    
    <div>
      <input 
        v-model="password" 
        type="password" 
        placeholder="Пароль" 
        @keyup.enter="login"
        style="margin: 5px; padding: 8px; font-size: 16px; width: 200px;"
      />
    </div>
    
    <div style="margin-top: 15px;">
      <button @click="login" :disabled="loading" style="margin: 5px; padding: 8px 16px; font-size: 16px;">
        {{ loading ? 'Загрузка...' : 'Войти' }}
      </button>
      <button @click="register" :disabled="loading" style="margin: 5px; padding: 8px 16px; font-size: 16px;">
        {{ loading ? 'Загрузка...' : 'Регистрация' }}
      </button>
    </div>
    
    <div v-if="errorMessage" style="color: red; margin-top: 15px;">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import axios from 'axios'

export default {
  name: 'LoginForm',
  emits: ['login-success'],
  
  setup(props, { emit }) {
    const username = ref('')
    const password = ref('')
    const loading = ref(false)
    const errorMessage = ref('')

    const api = axios.create({
      baseURL: '',
      headers: {
        'Content-Type': 'application/json'
      }
    })

    const login = async () => {
      if (!username.value || !password.value) {
        errorMessage.value = 'Введите логин и пароль'
        return
      }

      await authenticate('/api/auth/login', 'входа')
    }

    const register = async () => {
      if (!username.value || !password.value) {
        errorMessage.value = 'Введите логин и пароль'
        return
      }

      await authenticate('/api/auth/register', 'регистрации')
    }

    const authenticate = async (url, action) => {
      loading.value = true
      errorMessage.value = ''

      try {
        const response = await api.post(url, {
          username: username.value,
          password: password.value
        })

        if (response.data.message && response.data.message.includes('successful')) {
          emit('login-success', {
            id: response.data.id,
            username: response.data.username,
            role: response.data.role
          })
          username.value = ''
          password.value = ''
        } else {
          errorMessage.value = response.data.message || `Ошибка ${action}`
        }
      } catch (error) {
        console.error('Ошибка:', error)
        
        if (error.response && error.response.data) {
          errorMessage.value = error.response.data.message || `Ошибка ${action}`
        } else if (error.request) {
          errorMessage.value = 'Не удалось подключиться к серверу'
        } else {
          errorMessage.value = `Ошибка ${action}: ${error.message}`
        }
      } finally {
        loading.value = false
      }
    }

    return {
      username,
      password,
      loading,
      errorMessage,
      login,
      register
    }
  }
}
</script>

<style scoped>
.login-form-old {
  border: 1px solid #ccc;
  padding: 20px;
  border-radius: 5px;
  margin-top: 20px;
  display: inline-block;
  text-align: center;
}

button {
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover:not(:disabled) {
  background-color: #45a049;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}
</style>