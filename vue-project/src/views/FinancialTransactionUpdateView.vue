<script setup>
import { watch } from 'vue';

import FinancialTransactionForm from '@/components/BankRecordOrFinancialTransactionForm/FinancialTransactionForm.vue';
import DataMessages from '@/components/DataMessages.vue';
import useDataStore from '@/store/DataStore';

const dataStore = useDataStore();

const {transactionId} = defineProps(["transactionId"]);

watch(() => dataStore.retrievalStatus,
  (newRetrievalStatus) => {
    if (newRetrievalStatus === 'NOT LOADED') {
      dataStore.loadSingleFinancialTransaction(transactionId);
    }
  },
  {immediate: true});
</script>

<template>
  <DataMessages :retrievalStatus="dataStore.retrievalStatus"
  loadingMessage="Loading Transaction..." errorMessage="Could Not Load Transaction">
    <FinancialTransactionForm :transaction="dataStore.data.financialTransaction"/>
  </DataMessages>
</template>