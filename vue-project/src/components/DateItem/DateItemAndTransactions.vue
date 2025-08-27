<script setup>
/*
* DateItemAndTransactions displays the date followed by
* A list of financial transactions
*/
import DateItem from './DateItem.vue';

const { accountId, dateAndTransactions } = defineProps(["accountId", "dateAndTransactions"]);
</script>

<template>
    <DateItem
        :accountId="accountId"
        :data="dateAndTransactions"
        :displayBankRecords="false"
    />
</template>