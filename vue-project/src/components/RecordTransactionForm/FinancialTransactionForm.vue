<script setup>
/*
* FinancialTransactionForm displays a form to update or create
* A financial transaction
*/
import RecordTransactionForm from './RecordTransactionForm.vue';

const { accountId, transaction } = defineProps(["accountId", "transaction"]);
</script>

<template>
    <RecordTransactionForm
        :accountId="accountId"
        :data="transaction"
        :isBankRecord="false"
    />
</template>