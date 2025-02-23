<script setup>
/*
* BankRecordForm displays a form to update or create a bank record
* Props
* - record (BankRecordModel to update or null to create)
*/
import BankRecordOrFinancialTransactionForm from './BankRecordOrFinancialTransactionForm.vue';

const { record } = defineProps(["record"]);
</script>

<template>
    <BankRecordOrFinancialTransactionForm
        :data="record"
        :isBankRecord="true"
    />
</template>