<script setup>
import BankRecordForm from '@/components/BankRecordForm.vue';
import DataMessages from '@/components/DataMessages.vue';
import useDataStore from '@/store/DataStore';
import { watch } from 'vue';

const {recordId} = defineProps(["recordId"]);

const dataStore = useDataStore();

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

<style scoped>
</style>