<script setup>
import BankRecordDetails from '@/components/BankRecordDetails.vue';
import DataMessages from '@/components/DataMessages.vue';
import useDataStore from '@/store/DataStore';
import { watch } from 'vue';

const {recordId} = defineProps(["recordId"]);

const dataStore = useDataStore();

watch(() => dataStore.retrievalStatus,
  (newRetrievalStatus) => {
    if (newRetrievalStatus === 'NOT LOADED') {
      dataStore.loadRecord(recordId);
    }
  },
  {immediate: true});
</script>

<template>
    <DataMessages :retrievalStatus="dataStore.retrievalStatus"
    loadingMessage="Loading Record..." errorMessage="Could Not Load Record">
    <BankRecordDetails :record="dataStore.data"/>
    </DataMessages>
</template>

<style scoped>
</style>