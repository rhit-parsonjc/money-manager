<script setup>
/*
* FinancialTransactionItem displays an item representing a
* single financial transaction
* Props
* - recordId (id of record to attach/detach)
* - transaction (FinancialTransactionModel)
* - isAttached (whether record is already attached)
*/
import BankRecordOrFinancialTransactionItem from './BankRecordOrFinancialTransactionItem.vue';

const { recordId, transaction, isAttached } = defineProps(["recordId", "transaction", "isAttached"]);
</script>

<template>
    <BankRecordOrFinancialTransactionItem
        :id="recordId"
        :data="transaction"
        :isBankRecord="false"
        :isAttached="isAttached"
    />
</template>