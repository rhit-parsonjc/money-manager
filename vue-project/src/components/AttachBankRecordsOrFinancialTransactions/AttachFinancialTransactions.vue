<script setup>
/*
* AttachFinancialTransactions attaches a single bank record
* To one or more financial transactions
* Props
* - bankRecord (BankRecordModel)
* - financialTransactions (FinancialTransactionModel list)
*/
import AttachBankRecordsOrFinancialTransactions from './AttachBankRecordsOrFinancialTransactions.vue';
const { bankRecord, financialTransactions } = defineProps(["bankRecord", "financialTransactions"]);
</script>

<template>
    <AttachBankRecordsOrFinancialTransactions
        :item="bankRecord"
        :subitems="financialTransactions"
        :isBankRecord="true"
    />
</template>