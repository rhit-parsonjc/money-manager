<script setup>
import DateRecord from './DateRecord.vue'
import { organizeRecordsByDate } from '@/model/DateRecordModel'

const { bankRecords, dateAmounts } = defineProps(["bankRecords", "dateAmounts"])
</script>

<template>
    <ul id="dateRecordList">
      <DateRecord
        v-for="(dateRecord, i) of organizeRecordsByDate(bankRecords, dateAmounts)"
        :dateRecord="dateRecord"
        :key={i}
      />
    </ul>
</template>