<script setup>
import { watch, ref } from 'vue';
import { useRouter } from 'vue-router';

import DataMessages from '@/components/DataMessages.vue';
import useDataStore from '@/store/DataStore';
import DateItemAndRecordsList from '@/components/DateItemList/DateItemAndRecordsList.vue';
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

function goToCreateRecordPage() {
  dataStore.expireData();
  router.push("/create/record").then(dataStore.resetData);
}

function reloadData(criterionInfo) {
  criterionType.value = criterionInfo.criterionType
  criterion.value = criterionInfo.criterion
  dataStore.resetData();
}

function loadData() {
  switch (criterionType.value) {
    case "None":
      dataStore.loadDateAndBankRecords();
      break;
    case "Year":
      dataStore.loadDateAndBankRecordsDuringYear(criterion.value.year);
      break;
    case "Month":
      dataStore.loadDateAndBankRecordsDuringMonth(criterion.value.year, criterion.value.month);
      break;
    case "Day":
      dataStore.loadDateAndBankRecordsDuringDay(criterion.value.year, criterion.value.month, criterion.value.day);
      break;
  }
}

async function populateData() {
  await dataStore.populateDataAsync();
}
</script>

<template>
  <h1 class="libre-baskerville-regular">Records</h1>
  <a @click="goToCreateRecordPage" class="libre-baskerville-regular AllBankRecordsView-add-button">Add New Bank Record</a>
  <DateFilter @applyFilter="reloadData" />
  <button @click="populateData">Populate Data</button>
  <DataMessages :retrievalStatus="dataStore.retrievalStatus"
  loadingMessage="Loading Records..." errorMessage="Could Not Load Records">
    <DateItemAndRecordsList
      :bankRecords="dataStore.data.bankRecords"
      :dateAmounts="dataStore.data.dateAmounts"
      :criterionType="criterionType"
      :criterion="criterion"/>
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