<template>
  <div class="admin-panel">
    <h1>Панель администратора</h1>
    
    <div class="admin-tabs">
      <button 
        v-for="tab in tabs" 
        :key="tab.id"
        @click="currentTab = tab.id"
        :class="['tab-btn', { active: currentTab === tab.id }]"
      >
        {{ tab.label }}
      </button>
    </div>
    <div class="tab-content">
      <component :is="currentTabComponent" :adminUserId="userId" @refresh-needed="refreshData" />
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import AdminPlayersTab from '../components/admin/AdminPlayersTab.vue'
import AdminMatchesTab from '../components/admin/AdminMatchesTab.vue'
import AdminUsersTab from '../components/admin/AdminUsersTab.vue'

export default {
  name: 'AdminPanel',
  props: {
    userId: {
      type: Number,
      required: true
    }
  },
  
  components: {
    AdminPlayersTab,
    AdminMatchesTab,
    AdminUsersTab
  },
  
  setup(props) {
    const tabs = ref([
      { id: 'players', label: 'Игроки' },
      { id: 'matches', label: 'Матчи' },
      { id: 'users', label: 'Пользователи' }
    ])
    
    const currentTab = ref('players')
    
    const currentTabComponent = computed(() => {
      switch(currentTab.value) {
        case 'players': return 'AdminPlayersTab'
        case 'matches': return 'AdminMatchesTab'
        case 'users': return 'AdminUsersTab'
        default: return 'AdminPlayersTab'
      }
    })

    const refreshData = () => {
    }
    
    return {
      tabs,
      currentTab,
      currentTabComponent,
      refreshData
    }
  }
}
</script>

<style scoped>
.admin-panel {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  color: #313030ff;
  margin-bottom: 30px;
  text-align: center;
  border-bottom: 2px solid #ddd;
  padding-bottom: 10px;
}

.admin-tabs {
  display: flex;
  justify-content: center;
  gap: 5px;
  margin-bottom: 30px;
  flex-wrap: wrap;
  background-color: #f8f9fa;
  padding: 5px;
  border-radius: 6px;
}

.tab-btn {
  padding: 10px 20px;
  background-color: transparent;
  border: 1px solid transparent;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  color: #666;
  transition: all 0.2s;
}

.tab-btn:hover {
  background-color: rgba(33, 150, 243, 0.1);
  color: #2196F3;
}

.tab-btn.active {
  background-color: rgba(33, 150, 243, 0.2);
  color: #2196F3;
  border-color: rgba(33, 150, 243, 0.3);
}

.tab-content {
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  min-height: 400px;
}
</style>