import BankRecordsView from './views/BankRecordsView.vue'
import BankRecordDetailsView from './views/BankRecordDetailsView.vue'
import BankRecordUpdateView from './views/BankRecordUpdateView.vue'
import BankRecordCreateView from './views/BankRecordCreateView.vue'
import { createRouter, createWebHistory } from 'vue-router'
import FinancialTransactionCreateView from './views/FinancialTransactionCreateView.vue'
import FinancialTransactionDetailsView from './views/FinancialTransactionDetailsView.vue'
import FinancialTransactionUpdateView from './views/FinancialTransactionUpdateView.vue'
import FinancialTransactionsView from './views/FinancialTransactionsView.vue'
import AttachRecordsView from './views/AttachRecordsView.vue'
import AttachTransactionsView from './views/AttachTransactionsView.vue'

const routes = [
  {
    path: '/create/record',
    name: 'record-create',
    component: BankRecordCreateView,
  },
  {
    path: '/records',
    name: 'records',
    component: BankRecordsView,
  },
  {
    path: '/records/:recordId',
    name: 'record',
    component: BankRecordDetailsView,
    props: true,
  },
  {
    path: '/records/:recordId/update',
    name: 'record-update',
    component: BankRecordUpdateView,
    props: true,
  },
  {
    path: '/records/:recordId/transactions',
    name: 'attach-transactions',
    component: AttachTransactionsView,
    props: true,
  },
  {
    path: '/create/transaction',
    name: 'transaction-create',
    component: FinancialTransactionCreateView,
  },
  {
    path: '/transactions',
    name: 'transactions',
    component: FinancialTransactionsView,
  },
  {
    path: '/transactions/:transactionId',
    name: 'transaction',
    component: FinancialTransactionDetailsView,
    props: true,
  },
  {
    path: '/transactions/:transactionId/update',
    name: 'transaction-update',
    component: FinancialTransactionUpdateView,
    props: true,
  },
  {
    path: '/transactions/:transactionId/records',
    name: 'attach-records',
    component: AttachRecordsView,
    props: true,
  },
]
const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
