<script setup>
/*
* AttachBankRecords attaches a single financial transaction
* To one or more bank records
* Props
* - financialTransaction (FinancialTransactionModel)
* - bankRecords (BankRecordModel list)
*/
import AttachBankRecordsOrFinancialTransactions from './AttachBankRecordsOrFinancialTransactions.vue';
const { financialTransaction, bankRecords } = defineProps(["financialTransaction", "bankRecords"]);
</script>

<template>
    <AttachBankRecordsOrFinancialTransactions
        :item="financialTransaction"
        :subitems="bankRecords"
        :isBankRecord="false"
    />
</template>