<script setup>
import useRecordStore from './store/DataStore';
import DateRecord from './components/DateRecord.vue';

const recordStore = useRecordStore();
recordStore.loadData();
</script>

<template>
  <h1>Records</h1>
  <ul id="dateRecordList">
    <DateRecord
      v-for="(dateRecord, i) of recordStore.dateRecords"
      :dateRecord="dateRecord"
      :key={i}
    />
  </ul>
</template>

<style scoped>
#dateRecordList {
  list-style-type: none;
  padding-left: 0em;
}
</style>