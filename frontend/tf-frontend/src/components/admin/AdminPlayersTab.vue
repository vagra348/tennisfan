<template>
  <div class="admin-players-tab">
    <div class="tab-header">
      <h2>Управление игроками</h2>
      <button @click="showCreateForm = true" class="add-btn">
        + Добавить игрока
      </button>
    </div>
    
    <div v-if="showCreateForm" class="form-modal">
      <div class="form-content">
        <h3>{{ editingPlayer ? 'Редактировать игрока' : 'Создать игрока' }}</h3>
        
        <div class="form-group">
          <label>Полное имя *</label>
          <input v-model="playerForm.fullName" placeholder="Введите полное имя">
        </div>
        
        <div class="form-group">
          <label>Страна</label>
          <input v-model="playerForm.country" placeholder="Введите страну">
        </div>
        
        <div class="form-group">
          <label>Рейтинг</label>
          <input v-model="playerForm.ranking" type="number" placeholder="Введите рейтинг" min="1">
        </div>
        
        <div class="form-actions">
          <button @click="savePlayer" :disabled="!playerForm.fullName" class="save-btn">
            {{ editingPlayer ? 'Сохранить' : 'Создать' }}
          </button>
          <button @click="cancelForm" class="cancel-btn">Отмена</button>
        </div>
      </div>
    </div>
    
    <div v-if="loading" class="loading">Загрузка...</div>
    
    <div v-else class="players-table">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Имя</th>
            <th>Страна</th>
            <th>Рейтинг</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="player in players" :key="player.id">
            <td>{{ player.id }}</td>
            <td>{{ player.fullName }}</td>
            <td>{{ player.country || '-' }}</td>
            <td>{{ player.ranking || '-' }}</td>
            <td class="actions">
              <button @click="editPlayer(player)" class="edit-btn">Редактировать</button>
              <button @click="deletePlayer(player.id)" class="delete-btn">Удалить</button>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div v-if="players.length === 0" class="no-data">
        Нет данных об игроках
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'AdminPlayersTab',
  
  props: {
    adminUserId: {
      type: Number,
      required: true
    }
  },
  
  setup(props) {
    const players = ref([])
    const loading = ref(true)
    const showCreateForm = ref(false)
    const editingPlayer = ref(null)
    
    const playerForm = ref({
      fullName: '',
      country: '',
      ranking: null
    })
    
    const api = axios.create({
      baseURL: '',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    
    const loadPlayers = async () => {
      loading.value = true
      try {
        const response = await api.get('/api/admin/players', {
          headers: { 'X-User-Id': props.adminUserId }
        })
        players.value = response.data
      } catch (error) {
        console.error('Ошибка загрузки игроков:', error)
        if (error.response?.status === 403) {
          alert('У вас нет прав администратора')
        } else {
          alert('Не удалось загрузить игроков')
        }
      } finally {
        loading.value = false
      }
    }
    
    const editPlayer = (player) => {
      editingPlayer.value = player
      playerForm.value = {
        fullName: player.fullName,
        country: player.country || '',
        ranking: player.ranking
      }
      showCreateForm.value = true
    }
    
    const savePlayer = async () => {
      try {
        if (editingPlayer.value) {
          await api.put(`/api/admin/players/${editingPlayer.value.id}`, playerForm.value, {
            headers: { 'X-User-Id': props.adminUserId }
          })
        } else {
          await api.post('/api/admin/players', playerForm.value, {
            headers: { 'X-User-Id': props.adminUserId }
          })
        }
        
        await loadPlayers()
        cancelForm()
        alert(editingPlayer.value ? 'Игрок обновлён' : 'Игрок создан')
      } catch (error) {
        console.error('Ошибка сохранения игрока:', error)
        alert('Не удалось сохранить игрока')
      }
    }
    
    const deletePlayer = async (playerId) => {
      if (!confirm('Вы уверены, что хотите удалить этого игрока? Все связанные матчи также будут удалены.')) {
        return
      }
      
      try {
        await api.delete(`/api/admin/players/${playerId}`, {
          headers: { 'X-User-Id': props.adminUserId }
        })
        await loadPlayers()
        alert('Игрок удалён')
      } catch (error) {
        console.error('Ошибка удаления игрока:', error)
        alert('Не удалось удалить игрока')
      }
    }
    
    const cancelForm = () => {
      showCreateForm.value = false
      editingPlayer.value = null
      playerForm.value = {
        fullName: '',
        country: '',
        ranking: null
      }
    }
    
    onMounted(() => {
      loadPlayers()
    })
    
    return {
      players,
      loading,
      showCreateForm,
      editingPlayer,
      playerForm,
      loadPlayers,
      editPlayer,
      savePlayer,
      deletePlayer,
      cancelForm
    }
  }
}
</script>

<style scoped>
.admin-players-tab {
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

.add-btn {
  background-color: rgba(33, 150, 243, 0.1);
  color: #2196F3;
  border: 1px solid rgba(33, 150, 243, 0.3);
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.add-btn:hover {
  background-color: rgba(33, 150, 243, 0.2);
}

.form-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.form-content {
  background-color: white;
  padding: 30px;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.form-content h3 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #333;
  font-weight: normal;
  font-size: 18px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #555;
  font-size: 14px;
}

.form-group input {
  width: 100%;
  padding: 8px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  box-sizing: border-box;
}

.form-group input:focus {
  outline: none;
  border-color: #2196F3;
}

.form-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.save-btn, .cancel-btn {
  padding: 8px 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  flex: 1;
  transition: all 0.2s;
}

.save-btn {
  background-color: rgba(33, 150, 243, 0.1);
  color: #2196F3;
  border-color: rgba(33, 150, 243, 0.3);
}

.save-btn:hover:not(:disabled) {
  background-color: rgba(33, 150, 243, 0.2);
}

.save-btn:disabled {
  background-color: #f5f5f5;
  color: #999;
  border-color: #eee;
  cursor: not-allowed;
}

.cancel-btn {
  background-color: #f5f5f5;
  color: #666;
  border-color: #ddd;
}

.cancel-btn:hover {
  background-color: #e9e9e9;
}

.players-table {
  overflow-x: auto;
  margin-top: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

th, td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

th {
  color: #666;
  font-weight: 600;
  background-color: #f9f9f9;
}

tr:hover {
  background-color: #f5f5f5;
}

.actions {
  white-space: nowrap;
}

.edit-btn, .delete-btn {
  padding: 6px 12px;
  margin: 0 4px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
  transition: all 0.2s;
}

.edit-btn {
  background-color: rgba(33, 150, 243, 0.1);
  color: #2196F3;
  border-color: rgba(33, 150, 243, 0.3);
}

.edit-btn:hover {
  background-color: rgba(33, 150, 243, 0.2);
}

.delete-btn {
  background-color: rgba(244, 67, 54, 0.1);
  color: #f44336;
  border-color: rgba(244, 67, 54, 0.3);
}

.delete-btn:hover {
  background-color: rgba(244, 67, 54, 0.2);
}

.loading {
  text-align: center;
  padding: 40px;
  font-size: 16px;
  color: #666;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #999;
  font-style: italic;
  background-color: #f9f9f9;
  border-radius: 4px;
  margin-top: 20px;
}
</style>