<script setup>
/*
* BankRecord displays a single bank record to be used in a list view
* Props
* - record (BankRecordModel)
*/

import BankRecordOrFinancialTransaction from './BankRecordOrFinancialTransaction.vue';

const { record } = defineProps(["record"]);
</script>

<template>
    <BankRecordOrFinancialTransaction
        :data="record"
        :isBankRecord="true"
    />
</template>