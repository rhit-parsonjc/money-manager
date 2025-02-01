<script setup>
import DataMessages from '@/components/DataMessages.vue';
import useDataStore from '@/store/DataStore';
import { watch } from 'vue';
import { useRouter } from 'vue-router';
import DateRecordList from '@/components/DateRecordList.vue';

const dataStore = useDataStore();
const router = useRouter();

watch(() => dataStore.retrievalStatus,
  (newRetrievalStatus) => {
    if (newRetrievalStatus === 'NOT LOADED') {
      dataStore.loadDateAndBankRecords();
    }
  },
  {immediate: true})

function goToCreateRecordPage() {
  dataStore.expireData()
  router.push("/create/record").then(dataStore.resetData)
}
</script>

<template>
  <h1 class="libre-baskerville-regular">Records</h1>
  <a @click="goToCreateRecordPage" class="libre-baskerville-regular AllBankRecordsView-add-button">Add New Bank Record</a>
  <DataMessages :retrievalStatus="dataStore.retrievalStatus"
  loadingMessage="Loading Records..." errorMessage="Could Not Load Records">
    <DateRecordList :bankRecords="dataStore.data.bankRecords" :dateAmounts="dataStore.data.dateAmounts"/>
  </DataMessages>
</template>

<style scoped>
.AllBankRecordsView-add-button {
  text-decoration: none;
  color: #050;
  display: block;
  text-align: center;
}
</style>