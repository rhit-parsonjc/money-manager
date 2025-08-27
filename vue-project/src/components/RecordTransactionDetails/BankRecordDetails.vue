<script setup>
/*
* BankRecordDetails displays the details of a single bank record
*/
import RecordTransactionDetails from './RecordTransactionDetails.vue';

const { accountId, record } = defineProps(["accountId", "record"]);
</script>

<template>
    <RecordTransactionDetails
        :accountId="accountId"
        :data="record"
        :isBankRecord="true"
    />
</template>