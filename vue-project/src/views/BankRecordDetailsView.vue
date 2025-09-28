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
    <DataMessages :retrievalStatus="dataStore.dataStatus"
    loadingMessage="Loading Record..." errorMessage="Could Not Load Record">
        <BankRecordDetails :accountId="accountId" :record="dataStore.data.bankRecord"/>
    </DataMessages>
</template>