<script setup>
import { watch } from 'vue';
import { useRouter } from 'vue-router';

import FinancialTransactionForm from '@/components/RecordTransactionForm/FinancialTransactionForm.vue';
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
    <DataMessages :retrievalStatus="dataStore.dataStatus"
        loadingMessage="Loading Transaction..." errorMessage="Could Not Load Transaction">
        <FinancialTransactionForm
            :accountId="accountId"
            :transaction="dataStore.data.financialTransaction"
        />
    </DataMessages>
</template>