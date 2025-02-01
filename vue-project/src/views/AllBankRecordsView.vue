<script setup>
import { watch, ref, computed } from 'vue';
import { useRouter } from 'vue-router';

import DataMessages from '@/components/DataMessages.vue';
import useDataStore from '@/store/DataStore';
import DateRecordList from '@/components/DateRecordList.vue';

const dataStore = useDataStore();
const router = useRouter();

const yearValue = ref();
const monthValue = ref();
const dayValue = ref();
const criterionTypeValue = ref("None");

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

function loadData() {
  let parts = undefined;
  let yearPart = undefined;
  let monthPart = undefined;
  let dayPart = undefined;
  switch (criterionTypeValue.value) {
    case "None":
      criterionType.value = criterionTypeValue.value;
      criterion.value = {};
      dataStore.loadDateAndBankRecords();
      break;
    case "Year":
      if (yearValue.value === undefined) return;
      criterionType.value = criterionTypeValue.value;
      criterion.value = {year: yearValue.value};
      dataStore.loadDateAndBankRecordsDuringYear(criterion.value.year);
      break;
    case "Month":
      if (monthValue.value === undefined) return;
      parts = monthValue.value.split("-");
      yearPart = parseInt(parts[0]);
      monthPart = parseInt(parts[1]);
      criterionType.value = criterionTypeValue.value;
      criterion.value = {year: yearPart, month: monthPart};
      dataStore.loadDateAndBankRecordsDuringMonth(criterion.value.year, criterion.value.month);
      break;
    case "Day":
      if (dayValue.value === undefined) return;
      parts = dayValue.value.split("-");
      yearPart = parseInt(parts[0]);
      monthPart = parseInt(parts[1]);
      dayPart = parseInt(parts[2]);
      criterionType.value = criterionTypeValue.value;
      criterion.value = {year: yearPart, month: monthPart, day: dayPart};
      dataStore.loadDateAndBankRecordsDuringDay(criterion.value.year, criterion.value.month, criterion.value.day);
      break;
  }
}

const criterionInvalid = computed(() => {
  switch (criterionTypeValue.value) {
    case "None":
      return false;
    case "Year":
      return yearValue.value === undefined;
    case "Month":
      return monthValue.value === undefined;
    case "Day":
      return dayValue.value === undefined;
    default:
      return true;
  }
})
</script>

<template>
  <h1 class="libre-baskerville-regular">Records</h1>
  <a @click="goToCreateRecordPage" class="libre-baskerville-regular AllBankRecordsView-add-button">Add New Bank Record</a>
  <div class="AllBankRecordsView-filter">
    <p class="ubuntu-regular">Filter By:</p>
    <select class="ubuntu-regular" v-model="criterionTypeValue">
      <option key="None" class="ubuntu-regular">None</option>
      <option key="Year" class="ubuntu-regular">Year</option>
      <option key="Month" class="ubuntu-regular">Month</option>
      <option key="Day" class="ubuntu-regular">Day</option>
    </select>
    <input type="number" class="ubuntu-regular AllBankRecordsView-year-input" v-model="yearValue" v-if="criterionTypeValue === 'Year'">
    <input type="month" class="ubuntu-regular" v-model="monthValue" v-if="criterionTypeValue === 'Month'">
    <input type="date" class="ubuntu-regular" v-model="dayValue" v-if="criterionTypeValue === 'Day'">
    <button class="ubuntu-regular AllBankRecordsView-apply-filter" @click="dataStore.resetData" :disabled="criterionInvalid">Apply Filter</button>
  </div>
  <DataMessages :retrievalStatus="dataStore.retrievalStatus"
  loadingMessage="Loading Records..." errorMessage="Could Not Load Records">
    <DateRecordList :bankRecords="dataStore.data.bankRecords" :dateAmounts="dataStore.data.dateAmounts" :criterionType="criterionType" :criterion="criterion"/>
  </DataMessages>
</template>

<style scoped>
.AllBankRecordsView-add-button {
  text-decoration: none;
  color: #050;
  display: block;
  text-align: center;
}
.AllBankRecordsView-filter {
  display: flex;
}
.AllBankRecordsView-filter * {
  margin-right: 0.5em;
}
.AllBankRecordsView-filter *:last-child {
  margin-right: 0em;
}
.AllBankRecordsView-year-input {
  width: 5em;
}
.AllBankRecordsView-apply-filter {
  background-color: white;
}
</style>