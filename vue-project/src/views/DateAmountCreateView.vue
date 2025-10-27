<script setup>
import DateAmountForm from '@/components/DateAmountForm.vue';

const { accountId, yearValue, monthValue, dayValue } = defineProps(["accountId", "yearValue", "monthValue", "dayValue"]);
</script>

<template>
    <DateAmountForm
        :accountId="accountId"
        :yearValue="yearValue"
        :monthValue="monthValue"
        :dayValue="dayValue"
        :amountValue="null"
    />
</template>