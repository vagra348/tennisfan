<template>
  <div class="favorite-matches-page">
    <h1>Матчи избранных игроков</h1>
    
    <div v-if="loading" class="loading">Загрузка...</div>
    
    <div v-else-if="matches.length === 0" class="no-matches">
      Нет матчей с избранными игроками
    </div>
    
    <div v-else>
      <div class="matches-count">
        Найдено матчей: {{ matches.length }}
      </div>
      
      <MatchCard
        v-for="match in matches"
        :key="match.id"
        :match="match"
        :userId="userId"
        @favorite-changed="loadMatches"
      />
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import MatchCard from '../components/MatchCard.vue'

export default {
  name: 'FavoriteMatchesPage',
  components: {
    MatchCard
  },
  props: {
    userId: {
      type: Number,
      required: true
    }
  },
  
  setup(props) {
    const matches = ref([])
    const loading = ref(true)
    
    const api = axios.create({
      baseURL: '',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    
    const loadMatches = async () => {
      loading.value = true
      try {
        const response = await api.get('/api/matches/favorites', {
          params: { userId: props.userId }
        })
        matches.value = response.data
      } catch (error) {
        console.error('Ошибка загрузки матчей:', error)
        alert('Не удалось загрузить матчи')
      } finally {
        loading.value = false
      }
    }
    
    onMounted(() => {
      loadMatches()
    })
    
    return {
      matches,
      loading,
      loadMatches
    }
  }
}
</script>

<style scoped>
.favorite-matches-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  color: #2e2d2dff;
  margin-bottom: 30px;
  text-align: center;
}

.loading {
  text-align: center;
  padding: 40px;
  font-size: 18px;
  color: #6d6c6cff;
}

.no-matches {
  text-align: center;
  padding: 40px;
  font-size: 16px;
  color: #686767ff;
  background-color: #f5f5f5;
  border-radius: 8px;
}

.matches-count {
  margin-bottom: 20px;
  font-size: 14px;
  color: #676666ff;
}
</style>