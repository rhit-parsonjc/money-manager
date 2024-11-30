<script setup>
import { BankRecordModel } from './model/BankRecordModel';
import DateRecord from './components/DateRecord.vue';
import {organizeRecordsByDate} from "./model/DateRecordModel";

const records = [
  new BankRecordModel("Streaming Subscription", 2024, 10, 10, 25.00),
  new BankRecordModel("Vending Machine Snack", 2024, 10, 6, 5.00),
  new BankRecordModel("Tax Refund", 2024, 10, 7, -1.50),
  new BankRecordModel("Dinner", 2024, 10, 7, 15.50),
];
const dateRecords = organizeRecordsByDate(records);
</script>

<template>
  <h1>Records</h1>
  <ul id="dateRecordList">
    <DateRecord
      v-for="(dateRecord, i) of dateRecords"
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