<script setup>
import { watch } from 'vue';

import BankRecordForm from '@/components/BankRecordOrFinancialTransactionForm/BankRecordForm.vue';
import DataMessages from '@/components/DataMessages.vue';
import useDataStore from '@/store/DataStore';

const dataStore = useDataStore();

const {recordId} = defineProps(["recordId"]);

watch(() => dataStore.retrievalStatus,
  (newRetrievalStatus) => {
    if (newRetrievalStatus === 'NOT LOADED') {
      dataStore.loadSingleBankRecord(recordId);
    }
  },
  {immediate: true});
</script>

<template>
  <DataMessages :retrievalStatus="dataStore.retrievalStatus"
  loadingMessage="Loading Record..." errorMessage="Could Not Load Record">
    <BankRecordForm :record="dataStore.data.bankRecord"/>
  </DataMessages>
</template>