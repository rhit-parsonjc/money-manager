<script setup>
/*
* BankRecordForm displays a form to update or create a bank record
*/
import RecordTransactionForm from './RecordTransactionForm.vue';

const { accountId, record } = defineProps(["accountId", "record"]);
</script>

<template>
    <RecordTransactionForm
        :accountId="accountId"
        :data="record"
        :isBankRecord="true"
    />
</template>