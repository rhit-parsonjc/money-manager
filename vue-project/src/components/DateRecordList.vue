<script setup>
import DateRecord from './DateRecord.vue'
import { organizeRecordsByDate } from '@/model/DateRecordModel'

const { bankRecords } = defineProps(["bankRecords"])
</script>

<template>
    <ul id="dateRecordList">
      <DateRecord
        v-for="(dateRecord, i) of organizeRecordsByDate(bankRecords)"
        :dateRecord="dateRecord"
        :key={i}
      />
    </ul>
</template>