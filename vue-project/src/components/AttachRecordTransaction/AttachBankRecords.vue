<script setup>
/*
* AttachBankRecords attaches a single financial transaction
* To one or more bank records
*/
import AttachRecordTransactions from './AttachRecordTransactions.vue';

const { accountId, financialTransaction, bankRecords } = defineProps(["accountId", "financialTransaction", "bankRecords"]);
</script>

<template>
    <AttachRecordTransactions
        :accountId="accountId"
        :item="financialTransaction"
        :subitems="bankRecords"
        :isBankRecord="false"
    />
</template>