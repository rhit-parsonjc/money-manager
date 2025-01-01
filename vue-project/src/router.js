import AllBankRecordsView from './views/AllBankRecordsView.vue'
import BankRecordDetailsView from './views/BankRecordDetailsView.vue'
import BankRecordUpdateView from './views/BankRecordUpdateView.vue'
import BankRecordCreateView from './views/BankRecordCreateView.vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/create/record',
    name: 'record-create',
    component: BankRecordCreateView,
  },
  {
    path: '/records/:recordId/update',
    name: 'record-update',
    component: BankRecordUpdateView,
    props: true,
  },
  { path: '/records/:recordId', name: 'record', component: BankRecordDetailsView, props: true },
  { path: '/records', name: 'records', component: AllBankRecordsView },
]
const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
