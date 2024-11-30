<script setup>
import Record from './Record.vue';

const {dateRecord} = defineProps(["dateRecord"]);
</script>

<template>
    <li>
      <h2 class="dateRecordDate">{{dateRecord.dateObj.format()}}</h2>
      <Record v-for="record of dateRecord.records" :record="record"/>
    </li>
</template>

<style scoped>
.dateRecordDate {
  text-align: center;
}
</style>