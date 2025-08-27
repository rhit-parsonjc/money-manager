<script setup>
import { watch } from 'vue';

import BankRecordForm from '@/components/RecordTransactionForm/BankRecordForm.vue';
import DataMessages from '@/components/DataMessages.vue';
import useDataStore, { DataStatus } from '@/store/DataStore';

const dataStore = useDataStore();

const { accountId, recordId } = defineProps(["accountId", "recordId"]);

watch(() => dataStore.dataStatus,
    (newDataStatus) => {
        if (newDataStatus === DataStatus.NOT_LOADED) {
            dataStore.loadSingleBankRecordAsync(accountId, recordId);
        }
    },
    {immediate: true}
);

</script>

<template>
    <DataMessages :retrievalStatus="dataStore.dataStatus"
        loadingMessage="Loading Record..." errorMessage="Could Not Load Record">
        <BankRecordForm :accountId="accountId" :record="dataStore.data.bankRecord"/>
    </DataMessages>
</template>