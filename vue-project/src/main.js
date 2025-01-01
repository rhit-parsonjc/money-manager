import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import AllBankRecordsView from './views/AllBankRecordsView.vue'
import BankRecordDetailsView from './views/BankRecordDetailsView.vue'
import { createRouter, createWebHistory } from 'vue-router'

const app = createApp(App)

const pinia = createPinia()
app.use(pinia)

const routes = [
  { path: '/records/:recordId', name: 'record', component: BankRecordDetailsView, props: true },
  { path: '/records', name: 'records', component: AllBankRecordsView },
]
const router = createRouter({
  history: createWebHistory(),
  routes,
})
app.use(router)

app.mount('#app')
