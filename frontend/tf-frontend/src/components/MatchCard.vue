<template>
  <div class="match-card">
    <div class="players-row">
      <div class="player-column">
        <div class="player-name">{{ match.player1Name }}</div>
        <div class="player-score">{{ match.player1Score !== null ? match.player1Score : '-' }}</div>
        <button @click="toggleFavorite(match.player1Id)"
          :class="['favorite-btn', match.player1Favorite ? 'remove' : 'add']"
          :disabled="loading">
          {{ match.player1Favorite ? 'Удалить из избранного' : 'Добавить в избранное' }}
        </button>
      </div>
      
      <div class="vs">vs</div>

      <div class="player-column">
        <div class="player-name">{{ match.player2Name }}</div>
        <div class="player-score">{{ match.player2Score !== null ? match.player2Score : '-' }}</div>
        <button @click="toggleFavorite(match.player2Id)"
          :class="['favorite-btn', match.player2Favorite ? 'remove' : 'add']"
          :disabled="loading">
          {{ match.player2Favorite ? 'Удалить из избранного' : 'Добавить в избранное' }}
        </button>
      </div>
    </div>
    
    <div class="match-info">
      <div>Турнир: {{ match.tournamentName }}</div>
      <div>Дата: {{ formatDate(match.matchDate) }}</div>
      <div>Статус: 
        <span :class="['status', match.status.toLowerCase()]">
          {{ getStatusText(match.status) }}
        </span>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import axios from 'axios'

export default {
  name: 'MatchCard',
  props: {
    match: {
      type: Object,
      required: true
    },
    userId: {
      type: Number,
      required: true
    }
  },
  emits: ['favorite-changed'],
  
  setup(props, { emit }) {
    const loading = ref(false)
    
    const api = axios.create({
      baseURL: '',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    
    const toggleFavorite = async (playerId) => {
      loading.value = true
      
      try {
        if (props.match.player1Id === playerId && props.match.player1Favorite) {
          await api.delete(`/api/favorites/players/${playerId}?userId=${props.userId}`)
        } else if (props.match.player2Id === playerId && props.match.player2Favorite) {
          await api.delete(`/api/favorites/players/${playerId}?userId=${props.userId}`)
        } else {
          await api.post(`/api/favorites/players/${playerId}?userId=${props.userId}`)
        }
        
        emit('favorite-changed')
        
      } catch (error) {
        console.error('Ошибка при изменении избранного:', error)
        alert('Не удалось изменить избранное')
      } finally {
        loading.value = false
      }
    }
    
    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleDateString('ru-RU') + ' ' + date.toLocaleTimeString('ru-RU', { 
        hour: '2-digit', 
        minute: '2-digit' 
      })
    }
    
    const getStatusText = (status) => {
      const statusMap = {
        'Scheduled': 'Запланирован',
        'Live': 'В прямом эфире',
        'Completed': 'Завершён'
      }
      return statusMap[status] || status
    }
    
    return {
      loading,
      toggleFavorite,
      formatDate,
      getStatusText
    }
  }
}
</script>

<style scoped>
.match-card {
  border: 1px solid #dddadaff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  background-color: #ffffffff;
}

.players-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.player-column {
  flex: 1;
  text-align: center;
}

.player-name {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}

.player-score {
  font-size: 24px;
  font-weight: bold;
  color: #323232ff;
  margin-bottom: 10px;
}

.vs {
  margin: 0 20px;
  font-weight: bold;
  color: #605f5fff;
}

.favorite-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.favorite-btn.add {
  background-color: #4CAF50;
  color: white;
}

.favorite-btn.remove {
  background-color: #c5c1c1ff;
  color: #666;
}

.favorite-btn:disabled {
  background-color: #e0e0e0ff;
  cursor: not-allowed;
}

.favorite-btn.add:hover:not(:disabled) {
  background-color: #45a049;
}

.favorite-btn.remove:hover:not(:disabled) {
  background-color: #b5b3b3ff;
}

.match-info {
  border-top: 1px solid #eeeeeeff;
  padding-top: 15px;
  font-size: 14px;
  color: #666;
}

.match-info div {
  margin-bottom: 5px;
}

.status {
  padding: 2px 8px;
  border-radius: 4px;
  font-weight: bold;
}

.status.scheduled {
  background-color: #f3eba4ff;
  color: #333;
}

.status.live {
  background-color: #e4726aff;
  color: white;
}

.status.completed {
  background-color: #4caf50ff;
  color: white;
}
</style>