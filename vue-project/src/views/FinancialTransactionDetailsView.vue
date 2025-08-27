<script setup>
import { watch } from 'vue';

import FinancialTransactionDetails from '@/components/RecordTransactionDetails/FinancialTransactionDetails.vue';
import DataMessages from '@/components/DataMessages.vue';
import useDataStore, { DataStatus } from '@/store/DataStore';

const dataStore = useDataStore();

const { accountId, transactionId } = defineProps(["accountId", "transactionId"]);

watch(() => dataStore.dataStatus,
    (newDataStatus) => {
        if (newDataStatus === DataStatus.NOT_LOADED) {
            dataStore.loadSingleFinancialTransactionAsync(accountId, transactionId);
        }
    },
    {immediate: true}
);
</script>

<template>
    <DataMessages :retrievalStatus="dataStore.dataStatus"
        loadingMessage="Loading Transaction..." errorMessage="Could Not Load Transaction">
        <FinancialTransactionDetails :accountId="accountId" :transaction="dataStore.data.financialTransaction"/>
    </DataMessages>
</template>