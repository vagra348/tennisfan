<template>
  <div class="admin-matches-tab">
    <div class="tab-header">
      <h2>Управление матчами</h2>
      <button @click="showCreateForm = true" class="add-btn">
        + Добавить матч
      </button>
    </div>
    
    <div v-if="showCreateForm" class="form-modal">
      <div class="form-content">
        <h3>{{ editingMatch ? 'Редактировать матч' : 'Создать матч' }}</h3>
        
        <div class="form-row">
          <div class="form-group">
            <label>Турнир *</label>
            <select v-model="matchForm.tournamentId" required>
              <option value="">Выберите турнир</option>
              <option v-for="tournament in tournaments" :key="tournament.id" :value="tournament.id">
                {{ tournament.name }}
              </option>
            </select>
          </div>
          
          <div class="form-group">
            <label>Статус *</label>
            <select v-model="matchForm.status" required>
              <option value="">Выберите статус</option>
              <option value="Scheduled">Запланирован</option>
              <option value="Live">В прямом эфире</option>
              <option value="Completed">Завершён</option>
            </select>
          </div>
        </div>
        
        <div class="form-row">
          <div class="form-group">
            <label>Игрок 1 *</label>
            <select v-model="matchForm.player1Id" required>
              <option value="">Выберите игрока</option>
              <option v-for="player in players" :key="player.id" :value="player.id">
                {{ player.fullName }}
              </option>
            </select>
          </div>
          
          <div class="form-group">
            <label>Игрок 2 *</label>
            <select v-model="matchForm.player2Id" required>
              <option value="">Выберите игрока</option>
              <option v-for="player in players" :key="player.id" :value="player.id">
                {{ player.fullName }}
              </option>
            </select>
          </div>
        </div>
        
        <div class="form-row">
          <div class="form-group">
            <label>Счёт игрока 1</label>
            <input v-model="matchForm.player1Score" type="number" min="0" placeholder="Счёт">
          </div>
          
          <div class="form-group">
            <label>Счёт игрока 2</label>
            <input v-model="matchForm.player2Score" type="number" min="0" placeholder="Счёт">
          </div>
        </div>
        
        <div class="form-group">
          <label>Дата и время *</label>
          <input v-model="matchForm.matchDate" type="datetime-local" required>
        </div>
        
        <div class="form-actions">
          <button @click="saveMatch" :disabled="!isFormValid" class="save-btn">
            {{ editingMatch ? 'Сохранить' : 'Создать' }}
          </button>
          <button @click="cancelForm" class="cancel-btn">Отмена</button>
        </div>
      </div>
    </div>
    
    <div v-if="loading" class="loading">Загрузка...</div>
    
    <div v-else class="matches-table">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Турнир</th>
            <th>Игроки</th>
            <th>Счёт</th>
            <th>Дата</th>
            <th>Статус</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="match in matches" :key="match.id">
            <td>{{ match.id }}</td>
            <td>{{ getTournamentName(match.tournamentId) }}</td>
            <td>
              {{ getPlayerName(match.player1Id) }} vs {{ getPlayerName(match.player2Id) }}
            </td>
            <td>
              {{ match.player1Score !== null ? match.player1Score : '-' }} : 
              {{ match.player2Score !== null ? match.player2Score : '-' }}
            </td>
            <td>{{ formatDate(match.matchDate) }}</td>
            <td>
              <span :class="['status-badge', match.status.toLowerCase()]">
                {{ getStatusText(match.status) }}
              </span>
            </td>
            <td class="actions">
              <div class="action-buttons">
                <button @click="editMatch(match)" class="edit-btn">Редактировать</button>
                <button @click="deleteMatch(match.id)" class="delete-btn">Удалить</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div v-if="matches.length === 0" class="no-data">
        Нет данных о матчах
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'

export default {
  name: 'AdminMatchesTab',
  
  props: {
    adminUserId: {
      type: Number,
      required: true
    }
  },
  
  setup(props) {
    const matches = ref([])
    const players = ref([])
    const tournaments = ref([])
    const loading = ref(true)
    const showCreateForm = ref(false)
    const editingMatch = ref(null)
    
    const matchForm = ref({
      tournamentId: null,
      player1Id: null,
      player2Id: null,
      player1Score: null,
      player2Score: null,
      matchDate: '',
      status: ''
    })
    
    const api = axios.create({
      baseURL: '',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    
    const isFormValid = computed(() => {
      return matchForm.value.tournamentId && 
             matchForm.value.player1Id && 
             matchForm.value.player2Id &&
             matchForm.value.matchDate &&
             matchForm.value.status &&
             matchForm.value.player1Id !== matchForm.value.player2Id
    })
    
    const loadData = async () => {
      loading.value = true
      try {
        const [matchesRes, playersRes, tournamentsRes] = await Promise.all([
          api.get('/api/admin/matches', { headers: { 'X-User-Id': props.adminUserId } }),
          api.get('/api/players'),
          api.get('/api/tournaments')
        ])
        
        matches.value = matchesRes.data
        players.value = playersRes.data
        tournaments.value = tournamentsRes.data
      } catch (error) {
        console.error('Ошибка загрузки данных:', error)
        if (error.response?.status === 403) {
          alert('У вас нет прав администратора')
        } else {
          alert('Не удалось загрузить данные')
        }
      } finally {
        loading.value = false
      }
    }
    
    const getPlayerName = (playerId) => {
      const player = players.value.find(p => p.id === playerId)
      return player ? player.fullName : `Игрок #${playerId}`
    }
    
    const getTournamentName = (tournamentId) => {
      const tournament = tournaments.value.find(t => t.id === tournamentId)
      return tournament ? tournament.name : `Турнир #${tournamentId}`
    }
    
    const getStatusText = (status) => {
      const statusMap = {
        'Scheduled': 'Запланирован',
        'Live': 'В прямом эфире',
        'Completed': 'Завершён'
      }
      return statusMap[status] || status
    }
    
    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleDateString('ru-RU') + ' ' + 
             date.toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' })
    }
    
    const editMatch = (match) => {
      editingMatch.value = match
      matchForm.value = {
        tournamentId: match.tournamentId,
        player1Id: match.player1Id,
        player2Id: match.player2Id,
        player1Score: match.player1Score,
        player2Score: match.player2Score,
        matchDate: formatDateForInput(match.matchDate),
        status: match.status
      }
      showCreateForm.value = true
    }
    
    const formatDateForInput = (dateString) => {
      const date = new Date(dateString)
      return date.toISOString().slice(0, 16)
    }
    
    const saveMatch = async () => {
      if (matchForm.value.player1Id === matchForm.value.player2Id) {
        alert('Игроки должны быть разными!')
        return
      }
      
      try {
        const formData = {
          ...matchForm.value,
          player1Score: matchForm.value.player1Score || null,
          player2Score: matchForm.value.player2Score || null
        }
        
        if (editingMatch.value) {
          await api.put(`/api/admin/matches/${editingMatch.value.id}`, formData, {
            headers: { 'X-User-Id': props.adminUserId }
          })
        } else {
          await api.post('/api/admin/matches', formData, {
            headers: { 'X-User-Id': props.adminUserId }
          })
        }
        
        await loadData()
        cancelForm()
        alert(editingMatch.value ? 'Матч обновлён' : 'Матч создан')
      } catch (error) {
        console.error('Ошибка сохранения матча:', error)
        if (error.response?.data?.message) {
          alert(error.response.data.message)
        } else {
          alert('Не удалось сохранить матч')
        }
      }
    }
    
    const deleteMatch = async (matchId) => {
      if (!confirm('Вы уверены, что хотите удалить этот матч?')) {
        return
      }
      
      try {
        await api.delete(`/api/admin/matches/${matchId}`, {
          headers: { 'X-User-Id': props.adminUserId }
        })
        await loadData()
        alert('Матч удалён')
      } catch (error) {
        console.error('Ошибка удаления матча:', error)
        alert('Не удалось удалить матч')
      }
    }
    
    const cancelForm = () => {
      showCreateForm.value = false
      editingMatch.value = null
      matchForm.value = {
        tournamentId: null,
        player1Id: null,
        player2Id: null,
        player1Score: null,
        player2Score: null,
        matchDate: '',
        status: ''
      }
    }
    
    onMounted(() => {
      loadData()
    })
    
    return {
      matches,
      players,
      tournaments,
      loading,
      showCreateForm,
      editingMatch,
      matchForm,
      isFormValid,
      loadData,
      getPlayerName,
      getTournamentName,
      getStatusText,
      formatDate,
      editMatch,
      saveMatch,
      deleteMatch,
      cancelForm
    }
  }
}
</script>

<style scoped>
.admin-matches-tab {
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
  width: 500px;
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

.form-row {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
}

.form-row .form-group {
  flex: 1;
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

.form-group select,
.form-group input {
  width: 100%;
  padding: 8px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  box-sizing: border-box;
  background-color: white;
}

.form-group select:focus,
.form-group input:focus {
  outline: none;
  border-color: #2196F3;
}

.form-group input[type="datetime-local"] {
  width: 100%;
  padding: 8px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
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

/* Стили для таблицы матчей */
.matches-table {
  overflow-x: auto;
  margin-top: 20px;
}

.matches-table table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.matches-table th, .matches-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.matches-table th {
  color: #666;
  font-weight: 600;
  background-color: #f9f9f9;
}

.matches-table tr:hover {
  background-color: #f5f5f5;
}

/* Стили для кнопок действий в таблице матчей */
.matches-table .actions {
  white-space: nowrap;
}

.matches-table .action-buttons {
  display: flex;
  flex-direction: column;
  gap: 8px;
  align-items: flex-start;
}

.matches-table .edit-btn {
  padding: 6px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  background-color: #f8f9fa;
  color: #495057;
  transition: all 0.2s;
  width: 100px;
  text-align: center;
}

.matches-table .delete-btn {
  padding: 6px 12px;
  border: 1px solid #f1aeb5;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  background-color: #f8d7da;
  color: #721c24;
  transition: all 0.2s;
  width: 100px;
  text-align: center;
}

.matches-table .edit-btn:hover {
  background-color: #e9ecef;
  border-color: #adb5bd;
}

.matches-table .delete-btn:hover {
  background-color: #f1b0b7;
  border-color: #e97b86;
}

.status-badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: normal;
}

.status-badge.scheduled {
  background-color: rgba(255, 235, 59, 0.2);
  color: #f57c00;
}

.status-badge.live {
  background-color: rgba(244, 67, 54, 0.2);
  color: #d32f2f;
}

.status-badge.completed {
  background-color: rgba(76, 175, 80, 0.2);
  color: #388e3c;
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