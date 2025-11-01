<script setup>
import FinancialTransactionForm from '@/components/RecordTransactionForm/FinancialTransactionForm.vue';

const { accountId } = defineProps(["accountId"]);
</script>

<template>
    <div class="container-fluid p-3">
        <FinancialTransactionForm :accountId="accountId" :transaction="null"/>
    </div>
</template>