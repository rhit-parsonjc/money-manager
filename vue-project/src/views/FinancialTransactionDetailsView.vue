<script setup>
import { watch } from 'vue';
import { useRouter } from 'vue-router';

import FinancialTransactionDetails from '@/components/RecordTransactionDetails/FinancialTransactionDetails.vue';
import DataMessages from '@/components/DataMessages.vue';
import useDataStore, { DataStatus } from '@/store/DataStore';

const dataStore = useDataStore();
const router = useRouter();

const { accountId, transactionId } = defineProps(["accountId", "transactionId"]);

watch(() => dataStore.dataStatus,
    (newDataStatus) => {
        if (newDataStatus === DataStatus.NOT_LOADED) {
            dataStore.loadSingleFinancialTransactionAsync(accountId, transactionId).catch(err => {
                if (err === 'Unauthorized') {
                    router.push('/');
                }
            });
        }
    },
    {immediate: true}
);
</script>

<template>
    <div class="container-fluid p-3">
        <div class="row m-0 mb-2">
            <h1 class="libre-baskerville-regular text-center p-0">Transaction</h1>
        </div>
        <DataMessages :retrievalStatus="dataStore.dataStatus"
            loadingMessage="Loading Transaction..." errorMessage="Could Not Load Transaction">
            <FinancialTransactionDetails :accountId="accountId" :transaction="dataStore.data.financialTransaction"/>
        </DataMessages>
    </div>
</template>