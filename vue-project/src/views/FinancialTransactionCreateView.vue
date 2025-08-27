<script setup>
import FinancialTransactionForm from '@/components/RecordTransactionForm/FinancialTransactionForm.vue';

const { accountId } = defineProps(["accountId"]);
</script>

<template>
    <FinancialTransactionForm :accountId="accountId" :transaction="null"/>
</template>