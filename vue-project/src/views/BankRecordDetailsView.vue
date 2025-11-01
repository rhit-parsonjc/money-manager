<script setup>
import { watch } from 'vue';
import { useRouter } from 'vue-router';

import BankRecordDetails from '@/components/RecordTransactionDetails/BankRecordDetails.vue';
import DataMessages from '@/components/DataMessages.vue';
import useDataStore, { DataStatus } from '@/store/DataStore';

const dataStore = useDataStore();
const router = useRouter();

const { accountId, recordId } = defineProps(["accountId", "recordId"]);

watch(() => dataStore.dataStatus,
    (newDataStatus) => {
        if (newDataStatus === DataStatus.NOT_LOADED) {
            dataStore.loadSingleBankRecordAsync(accountId, recordId).catch(err => {
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
            <h1 class="libre-baskerville-regular text-center p-0">Record</h1>
        </div>
        <DataMessages :retrievalStatus="dataStore.dataStatus"
        loadingMessage="Loading Record..." errorMessage="Could Not Load Record">
            <BankRecordDetails :accountId="accountId" :record="dataStore.data.bankRecord"/>
        </DataMessages>
    </div>
</template>