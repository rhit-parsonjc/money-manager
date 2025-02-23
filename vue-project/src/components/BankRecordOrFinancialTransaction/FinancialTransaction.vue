<script setup>
/*
* FinancialTransaction displays a single financial transaction
* To be used in a list view
* Props
* - transaction (FinancialTransactionModel)
*/

import BankRecordOrFinancialTransaction from './BankRecordOrFinancialTransaction.vue';

const { transaction } = defineProps(["transaction"]);
</script>

<template>
    <BankRecordOrFinancialTransaction
        :data="transaction"
        :isBankRecord="false"
    />
</template>