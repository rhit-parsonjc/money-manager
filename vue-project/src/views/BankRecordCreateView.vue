<script setup>
import BankRecordForm from '@/components/RecordTransactionForm/BankRecordForm.vue';

const { accountId } = defineProps(["accountId"]);
</script>

<template>
    <BankRecordForm :record="null" :accountId="accountId"/>
</template>