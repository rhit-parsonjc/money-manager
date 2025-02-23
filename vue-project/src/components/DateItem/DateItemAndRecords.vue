<script setup>
/*
* DateItemAndRecords displays the date followed by
* A list of bank records
* Props
* - dateAndRecords (DateAndRecordsModel)
*/
import DateItem from './DateItem.vue';

const { dateAndRecords } = defineProps(["dateAndRecords"]);
</script>

<template>
    <DateItem
        :data="dateAndRecords"
        :displayBankRecords="true"
    />
</template>