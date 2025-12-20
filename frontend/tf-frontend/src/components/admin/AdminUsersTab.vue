<template>
  <div class="admin-users-tab">
    <div class="tab-header">
      <h2>Управление пользователями</h2>
    </div>
    
    <div v-if="loading" class="loading">Загрузка...</div>
    
    <div v-else class="users-table">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Логин</th>
            <th>Роль</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>
              <span :class="['role-badge', user.role.toLowerCase()]">
                {{ getRoleText(user.role) }}
              </span>
            </td>
            <td class="actions">
              <select v-if="user.id !== adminUserId" v-model="userRole[user.id]" @change="updateUserRole(user.id)" class="role-select">
                <option value="USER" :selected="user.role === 'USER'">Пользователь</option>
                <option value="ADMIN" :selected="user.role === 'ADMIN'">Администратор</option>
              </select>
              <span v-else class="current-user">Вы</span>
              
              <button v-if="user.id !== adminUserId" @click="deleteUser(user.id)" class="delete-btn">
                Удалить
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div v-if="users.length === 0" class="no-data">
        Нет данных о пользователях
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'AdminUsersTab',
  
  props: {
    adminUserId: {
      type: Number,
      required: true
    }
  },
  
  setup(props) {
    const users = ref([])
    const loading = ref(true)
    const userRole = ref({})
    
    const api = axios.create({
      baseURL: '',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    
    const loadUsers = async () => {
      loading.value = true
      try {
        const response = await api.get('/api/admin/users', {
          headers: { 'X-User-Id': props.adminUserId }
        })
        users.value = response.data
        
        response.data.forEach(user => {
          userRole.value[user.id] = user.role
        })
      } catch (error) {
        console.error('Ошибка загрузки пользователей:', error)
        if (error.response?.status === 403) {
          alert('У вас нет прав администратора')
        } else {
          alert('Не удалось загрузить пользователей')
        }
      } finally {
        loading.value = false
      }
    }
    
    const getRoleText = (role) => {
      return role === 'ADMIN' ? 'Администратор' : 'Пользователь'
    }
    
    const updateUserRole = async (userId) => {
      if (userId === props.adminUserId) {
        alert('Нельзя изменить свою собственную роль')
        return
      }
      
      const newRole = userRole.value[userId]
      const currentRole = users.value.find(u => u.id === userId)?.role
      
      if (newRole === currentRole) {
        return
      }
      
      try {
        await api.put(`/api/admin/users/${userId}/role`, 
          { role: newRole },
          { headers: { 'X-User-Id': props.adminUserId } }
        )
        
        const userIndex = users.value.findIndex(u => u.id === userId)
        if (userIndex !== -1) {
          users.value[userIndex].role = newRole
        }
        
        alert('Роль пользователя обновлена')
      } catch (error) {
        console.error('Ошибка обновления роли:', error)
        alert('Не удалось обновить роль пользователя')
        
        userRole.value[userId] = currentRole
      }
    }
    
    const deleteUser = async (userId) => {
      if (userId === props.adminUserId) {
        alert('Нельзя удалить самого себя')
        return
      }
      
      if (!confirm('Вы уверены, что хотите удалить этого пользователя?')) {
        return
      }
      
      try {
        await api.delete(`/api/admin/users/${userId}`, {
          headers: { 'X-User-Id': props.adminUserId }
        })
        await loadUsers()
        alert('Пользователь удалён')
      } catch (error) {
        console.error('Ошибка удаления пользователя:', error)
        alert('Не удалось удалить пользователя')
      }
    }
    
    onMounted(() => {
      loadUsers()
    })
    
    return {
      users,
      loading,
      userRole,
      loadUsers,
      getRoleText,
      updateUserRole,
      deleteUser
    }
  }
}
</script>

<style scoped>
.admin-users-tab {
  position: relative;
}

.tab-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.tab-header h2 {
  margin: 0;
  color: #333;
  font-size: 20px;
  font-weight: normal;
}

.role-badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: normal;
}

.role-badge.admin {
  background-color: rgba(76, 175, 80, 0.2);
  color: #388e3c;
}

.role-badge.user {
  background-color: rgba(33, 150, 243, 0.2);
  color: #1976d2;
}

.role-select {
  padding: 6px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 13px;
  margin-right: 8px;
  background-color: white;
  color: #333;
}

.role-select:focus {
  outline: none;
  border-color: #2196F3;
}

.current-user {
  color: #666;
  font-size: 13px;
  padding: 6px 10px;
  display: inline-block;
}

.delete-btn {
  padding: 6px 12px;
  border: 1px solid rgba(244, 67, 54, 0.3);
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
  background-color: rgba(244, 67, 54, 0.1);
  color: #f44336;
  transition: all 0.2s;
}

.delete-btn:hover {
  background-color: rgba(244, 67, 54, 0.2);
}

</style>