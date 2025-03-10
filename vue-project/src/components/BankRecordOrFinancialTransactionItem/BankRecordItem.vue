<script setup>
/*
* BankRecordItem displays an item representing a single bank record
* Props
* - transactionId (id of transaction to attach/detach)
* - record (BankRecordModel)
* - isAttached (whether transaction is already attached)
*/
import BankRecordOrFinancialTransactionItem from './BankRecordOrFinancialTransactionItem.vue';

const { transactionId, record, isAttached } = defineProps(["transactionId", "record", "isAttached"]);
</script>

<template>
    <BankRecordOrFinancialTransactionItem
        :id="transactionId"
        :data="record"
        :isBankRecord="true"
        :isAttached="isAttached"
    />
</template>