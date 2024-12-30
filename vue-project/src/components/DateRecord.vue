<script setup>
import BankRecord from './BankRecord.vue';

const {dateRecord} = defineProps(["dateRecord"]);
</script>

<template>
    <li>
      <h2 class="dateRecordDate">
        {{dateRecord.dateObj.format()}}
      </h2>
      <BankRecord
        v-for="record of dateRecord.records"
        :key="record.id"
        :record="record"
      />
    </li>
</template>

<style scoped>
.dateRecordDate {
  text-align: center;
}
</style>