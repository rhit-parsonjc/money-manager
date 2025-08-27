<script setup>
/*
* FinancialTransactionDetails displays the details
* Of a single financial transaction
*/
import RecordTransactionDetails from './RecordTransactionDetails.vue';

const { accountId, transaction } = defineProps(["accountId", "transaction"]);
</script>

<template>
    <RecordTransactionDetails
        :accountId="accountId"
        :data="transaction"
        :isBankRecord="false"
    />
</template>