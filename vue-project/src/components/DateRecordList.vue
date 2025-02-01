<script setup>
import DateRecord from './DateRecord.vue'
import { organizeRecordsByDate } from '@/model/DateRecordModel'

const { bankRecords, dateAmounts } = defineProps(["bankRecords", "dateAmounts"])
</script>

<template>
    <ul class="DateRecordList-list">
      <DateRecord
        v-for="(dateRecord, i) of organizeRecordsByDate(bankRecords, dateAmounts)"
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