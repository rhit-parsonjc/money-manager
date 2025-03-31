<script setup>
import { watch, ref } from 'vue';
import { useRouter } from 'vue-router';

import DataMessages from '@/components/DataMessages.vue';
import useDataStore from '@/store/DataStore';
import DateItemAndTransactionsList from '@/components/DateItemList/DateItemAndTransactionsList.vue';
import DateFilter from '@/components/DateFilter.vue';

const dataStore = useDataStore();
const router = useRouter();

const criterionType = ref("None");
const criterion = ref(null);

watch(() => dataStore.retrievalStatus,
  (newRetrievalStatus) => {
    if (newRetrievalStatus === 'NOT LOADED') {
      loadData();
    }
  },
  {immediate: true});

function goToCreateTransactionPage() {
  dataStore.expireData();
  router.push("/create/transaction").then(dataStore.resetData);
}

function reloadData(criterionInfo) {
  criterionType.value = criterionInfo.criterionType
  criterion.value = criterionInfo.criterion
  dataStore.resetData();
}

function loadData() {
  switch (criterionType.value) {
    case "None":
      dataStore.loadFinancialTransactions();
      break;
    case "Year":
      dataStore.loadFinancialTransactionsDuringYear(criterion.value.year);
      break;
    case "Month":
      dataStore.loadFinancialTransactionsDuringMonth(criterion.value.year, criterion.value.month);
      break;
    case "Day":
      dataStore.loadFinancialTransactionsDuringDay(criterion.value.year, criterion.value.month, criterion.value.day);
      break;
  }
}
</script>

<template>
  <h1 class="libre-baskerville-regular FinancialTransactionsView-header">Financial Transactions</h1>
  <a @click="goToCreateTransactionPage" class="ubuntu-regular FinancialTransactionsView-add-button">Add New Financial Transaction</a>
  <DateFilter @applyFilter="reloadData" />
  <DataMessages :retrievalStatus="dataStore.retrievalStatus"
  loadingMessage="Loading Transactions..." errorMessage="Could Not Load Transactions">
    <DateItemAndTransactionsList
        :financialTransactions="dataStore.data.financialTransactions"
        :criterionType="criterionType"
        :criterion="criterion"/>
  </DataMessages>
</template>

<style scoped>
.FinancialTransactionsView-header {
  text-align: center;
  text-decoration: underline;
  margin-bottom: 1rem;
}
.FinancialTransactionsView-add-button {
  text-decoration: none;
  color: #055;
  display: block;
  text-align: center;
  margin-bottom: 0.5rem;
}
</style>