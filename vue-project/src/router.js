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
import HomeView from './views/HomeView.vue'
import LoginView from './views/LoginView.vue'
import RegisterView from './views/RegisterView.vue'
import AccountsView from './views/AccountsView.vue'
import AccountView from './views/AccountView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView,
  },
  {
    path: '/accounts',
    name: 'accounts',
    component: AccountsView,
  },
  {
    path: '/accounts/:accountId',
    name: 'account',
    component: AccountView,
    props: true,
  },
  {
    path: '/accounts/:accountId/create/record',
    name: 'record-create',
    component: BankRecordCreateView,
    props: true,
  },
  {
    path: '/accounts/:accountId/records',
    name: 'records',
    component: BankRecordsView,
    props: true,
  },
  {
    path: '/accounts/:accountId/records/:recordId',
    name: 'record',
    component: BankRecordDetailsView,
    props: true,
  },
  {
    path: '/accounts/:accountId/records/:recordId/update',
    name: 'record-update',
    component: BankRecordUpdateView,
    props: true,
  },
  {
    path: '/accounts/:accountId/records/:recordId/transactions',
    name: 'attach-transactions',
    component: AttachTransactionsView,
    props: true,
  },
  {
    path: '/accounts/:accountId/create/transaction',
    name: 'transaction-create',
    component: FinancialTransactionCreateView,
    props: true,
  },
  {
    path: '/accounts/:accountId/transactions',
    name: 'transactions',
    component: FinancialTransactionsView,
    props: true,
  },
  {
    path: '/accounts/:accountId/transactions/:transactionId',
    name: 'transaction',
    component: FinancialTransactionDetailsView,
    props: true,
  },
  {
    path: '/accounts/:accountId/transactions/:transactionId/update',
    name: 'transaction-update',
    component: FinancialTransactionUpdateView,
    props: true,
  },
  {
    path: '/accounts/:accountId/transactions/:transactionId/records',
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
