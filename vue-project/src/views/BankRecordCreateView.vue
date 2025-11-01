<script setup>
import BankRecordForm from '@/components/RecordTransactionForm/BankRecordForm.vue';

const { accountId } = defineProps(["accountId"]);
</script>

<template>
    <div class="container-fluid p-3">
        <BankRecordForm :record="null" :accountId="accountId"/>
    </div>
</template>