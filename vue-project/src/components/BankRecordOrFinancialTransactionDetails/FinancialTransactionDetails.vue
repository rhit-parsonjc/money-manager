<script setup>
/*
* FinancialTransactionDetails displays the details
* Of a single financial transaction
* Props
* - transaction (FinancialTransactionModel)
*/
import BankRecordOrFinancialTransactionDetails from './BankRecordOrFinancialTransactionDetails.vue';

const { transaction } = defineProps(["transaction"]);
</script>

<template>
    <BankRecordOrFinancialTransactionDetails
        :data="transaction"
        :isBankRecord="false"
    />
</template>