<script setup>
/*
* BankRecordDetails displays the details of a single bank record
* Props
* - record (BankRecordModel)
*/
import BankRecordOrFinancialTransactionDetails from './BankRecordOrFinancialTransactionDetails.vue';

const { record } = defineProps(["record"]);
</script>

<template>
    <BankRecordOrFinancialTransactionDetails
        :data="record"
        :isBankRecord="true"
    />
</template>