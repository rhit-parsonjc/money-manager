<script setup>
/*
* BankRecord displays a single bank record to be used in a list view
*/

import RecordTransaction from './RecordTransaction.vue';

const { accountId, record } = defineProps(["accountId", "record"]);
</script>

<template>
    <RecordTransaction
        :accountId="accountId"
        :data="record"
        :isBankRecord="true"
    />
</template>