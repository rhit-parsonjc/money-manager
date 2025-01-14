<script setup>
/**
 * DateRecord displays a single date record, comprised of
 * a date plus a list of bank records.
 * Props:
 * - dateRecord (type DateRecordModel)
 */
import BankRecord from './BankRecord.vue'

const {dateRecord} = defineProps(["dateRecord"])
</script>

<template>
    <li>
      <h2 class="dateRecordDate happy-monkey-regular">
        {{dateRecord.dateObj.format()}}
      </h2>
      <ul class="bankRecordsPerDate">
        <BankRecord
          v-for="record of dateRecord.records"
          :key="record.id"
          :record="record"
        />
      </ul>
    </li>
</template>

<style scoped>
.dateRecordDate {
  text-align: left;
  font-size: 18pt;
  background-color: #0c0;
  padding: 0.25em;
  margin: 0.5rem 0em;
}
.bankRecordsPerDate {
  list-style-type: none;
  padding-left: 1rem;
}
</style>