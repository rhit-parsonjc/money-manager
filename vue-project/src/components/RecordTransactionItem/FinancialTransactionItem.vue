<script setup>
/*
* FinancialTransactionItem displays an item representing a
* single financial transaction
*/
import BankRecordOrFinancialTransactionItem from './RecordTransactionItem.vue';

const { accountId, recordId, transaction, isAttached } = defineProps(["accountId", "recordId", "transaction", "isAttached"]);
</script>

<template>
    <BankRecordOrFinancialTransactionItem
        :accountId="accountId"
        :id="recordId"
        :data="transaction"
        :isBankRecord="false"
        :isAttached="isAttached"
    />
</template>