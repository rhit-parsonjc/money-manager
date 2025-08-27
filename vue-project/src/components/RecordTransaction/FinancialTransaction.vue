<script setup>
/*
* FinancialTransaction displays a single financial transaction
* To be used in a list view
*/

import RecordTransaction from './RecordTransaction.vue';

const { accountId, transaction } = defineProps(["accountId", "transaction"]);
</script>

<template>
    <RecordTransaction
        :accountId="accountId"
        :data="transaction"
        :isBankRecord="false"
    />
</template>