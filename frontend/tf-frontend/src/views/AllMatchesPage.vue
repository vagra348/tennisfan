<template>
  <div class="all-matches-page">
    <h1>Все матчи</h1>
    
    <div v-if="loading" class="loading">Загрузка...</div>
    
    <div v-else-if="matches.length === 0" class="no-matches">
      Нет доступных матчей
    </div>
    
    <div v-else>
      <div class="matches-count">
        Всего матчей: {{ matches.length }}
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
  name: 'AllMatchesPage',
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
        const response = await api.get('/api/matches', {
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
.all-matches-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  color: #313030ff;
  margin-bottom: 30px;
  text-align: center;
}

.loading {
  text-align: center;
  padding: 40px;
  font-size: 18px;
  color: #6c6969ff;
}

.no-matches {
  text-align: center;
  padding: 40px;
  font-size: 16px;
  color: #5c5b5bff;
  background-color: #ffffffff;
  border-radius: 8px;
}

.matches-count {
  margin-bottom: 20px;
  font-size: 14px;
  color: #606060ff;
}
</style>