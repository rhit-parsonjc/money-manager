<script setup>
import { computed } from "vue";
import DateRecord from './DateRecord.vue';
import { organizeRecordsByDate } from '@/model/DateRecordModel';
import { DateObjectModel, getFirstDayOfYear, getLastDayOfYear, getFirstDayOfMonth, getLastDayOfMonth } from "@/model/DateObjectModel";

const { bankRecords, dateAmounts, criterionType, criterion } = defineProps(["bankRecords", "dateAmounts", "criterionType", "criterion"]);

const dateRecords = computed(() => {
  switch (criterionType) {
    case "None":
      return organizeRecordsByDate(bankRecords, dateAmounts);
    case "Year":
      return organizeRecordsByDate(
        bankRecords,
        dateAmounts,
        getFirstDayOfYear(criterion.year),
        getLastDayOfYear(criterion.year));
    case "Month":
      return organizeRecordsByDate(
        bankRecords,
        dateAmounts,
        getFirstDayOfMonth(criterion.year, criterion.month),
        getLastDayOfMonth(criterion.year, criterion.month));
    case "Day":
      return organizeRecordsByDate(
        bankRecords,
        dateAmounts,
        new DateObjectModel(criterion.year, criterion.month, criterion.day),
        new DateObjectModel(criterion.year, criterion.month, criterion.day));
    default:
      return [];  
  }
})
</script>

<template>
    <ul class="DateRecordList-list">
      <DateRecord
        v-for="(dateRecord, i) of dateRecords"
        :dateRecord="dateRecord"
        :key={i}
      />
    </ul>
</template>

<style scoped>
.DateRecordList-list {
  list-style-type: none;
  padding-left: 0rem;
}
</style>