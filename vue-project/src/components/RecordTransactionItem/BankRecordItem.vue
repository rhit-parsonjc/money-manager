<script setup>
/*
* BankRecordItem displays an item representing a single bank record
*/
import BankRecordOrFinancialTransactionItem from './RecordTransactionItem.vue';

const { accountId, transactionId, record, isAttached } = defineProps(["accountId", "transactionId", "record", "isAttached"]);
</script>

<template>
    <BankRecordOrFinancialTransactionItem
        :accountId="accountId"
        :id="transactionId"
        :data="record"
        :isBankRecord="true"
        :isAttached="isAttached"
    />
</template>