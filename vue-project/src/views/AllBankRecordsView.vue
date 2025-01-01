<script setup>
import DateRecord from '@/components/DateRecord.vue';
import useDataStore from '@/store/DataStore';
import { organizeRecordsByDate } from '@/model/DateRecordModel';
import { watch } from 'vue';
import { RouterLink } from 'vue-router';

const dataStore = useDataStore();
dataStore.loadRecords();

watch(() => dataStore.retrievalStatus,
  (newRetrievalStatus) => {
    if (newRetrievalStatus === 'NONE') {
      dataStore.loadRecords();
    }
  },
  {immediate: true});
</script>

<template>
  <h1 class="libre-baskerville-regular">Records</h1>
  <RouterLink to="/create/record" class="libre-baskerville-regular" id="addNewRecordButton">Add New Record</RouterLink>
  <p v-if="dataStore.retrievalStatus === 'LOADING'">
    Loading Records...
  </p>
  <p v-else-if="dataStore.retrievalStatus === 'ERROR'">
    Could Not Load Records
  </p>
  <ul id="dateRecordList"
    v-else-if="dataStore.retrievalStatus === 'DONE'">
    <DateRecord
      v-for="(dateRecord, i) of organizeRecordsByDate(dataStore.data)"
      :dateRecord="dateRecord"
      :key={i}
    />
  </ul>
</template>

<style scoped>
#dateRecordList {
  list-style-type: none;
  padding-left: 0rem;
}
#addNewRecordButton {
  text-decoration: none;
  color: #050;
  display: block;
  text-align: center;
}
#addNewRecordButton:hover {
  text-decoration: underline;
}
</style>