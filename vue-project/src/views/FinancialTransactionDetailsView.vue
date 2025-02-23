<script setup>
import { watch } from 'vue';

import FinancialTransactionDetails from '@/components/BankRecordOrFinancialTransactionDetails/FinancialTransactionDetails.vue';
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
    <FinancialTransactionDetails :transaction="dataStore.data.financialTransaction"/>
  </DataMessages>
</template>