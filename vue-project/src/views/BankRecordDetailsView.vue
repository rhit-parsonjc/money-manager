<script setup>
import { watch } from 'vue';

import BankRecordDetails from '@/components/BankRecordDetails.vue';
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
    <BankRecordDetails :record="dataStore.data.bankRecord"/>
    </DataMessages>
</template>