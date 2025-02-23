<script setup>
/*
* DateItemAndTransactions displays the date followed by
* A list of financial transactions
* Props
* - dateAndTransactions (DateAndTransactionsModel)
*/
import DateItem from './DateItem.vue';

const { dateAndTransactions } = defineProps(["dateAndTransactions"]);
</script>

<template>
    <DateItem
        :data="dateAndTransactions"
        :displayBankRecords="false"
    />
</template>