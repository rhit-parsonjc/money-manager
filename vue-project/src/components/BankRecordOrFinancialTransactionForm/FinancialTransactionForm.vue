<script setup>
/*
* FinancialTransactionForm displays a form to update or create
* A financial transaction
* Props
* - transaction (FinancialTransactionModel to update or null to create)
*/
import BankRecordOrFinancialTransactionForm from './BankRecordOrFinancialTransactionForm.vue';

const { transaction } = defineProps(["transaction"]);
</script>

<template>
    <BankRecordOrFinancialTransactionForm
        :data="transaction"
        :isBankRecord="false"
    />
</template>