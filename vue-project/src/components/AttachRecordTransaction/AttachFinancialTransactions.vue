<script setup>
/*
* AttachFinancialTransactions attaches a single bank record
* To one or more financial transactions
*/
import AttachRecordTransactions from './AttachRecordTransactions.vue';

const { accountId, bankRecord, financialTransactions } = defineProps(["accountId", "bankRecord", "financialTransactions"]);
</script>

<template>
    <AttachRecordTransactions
        :accountId="accountId"
        :item="bankRecord"
        :subitems="financialTransactions"
        :isBankRecord="true"
    />
</template>