<script setup>
/*
* DateItemAndRecords displays the date followed by
* A list of bank records
*/
import DateItem from './DateItem.vue';

const { accountId, dateAndRecords } = defineProps(["accountId", "dateAndRecords"]);
</script>

<template>
    <DateItem
        :accountId="accountId"
        :data="dateAndRecords"
        :displayBankRecords="true"
    />
</template>