<script setup>
import { watch, ref } from 'vue';

import AttachBankRecords from '@/components/AttachRecordTransaction/AttachBankRecords.vue';
import DataMessages from '@/components/DataMessages.vue';
import DateFilter from '@/components/DateFilter.vue';
import useDataStore, { DataStatus } from '@/store/DataStore';

const dataStore = useDataStore();

const criterionType = ref("None");
const criterion = ref(null);

const { accountId, transactionId } = defineProps(["accountId", "transactionId"]);

watch(() => dataStore.dataStatus,
    (newDataStatus) => {
        if (newDataStatus === DataStatus.NOT_LOADED) {
            loadData();
        }
    },
    {immediate: true});

function reloadData(criterionInfo) {
    criterionType.value = criterionInfo.criterionType;
    criterion.value = criterionInfo.criterion;
    dataStore.resetData();
}

function loadData() {
    switch (criterionType.value) {
        case "None":
            dataStore.loadFinancialTransactionAndBankRecordsAsync(accountId, transactionId);
            break;
        case "Year":
            dataStore.loadFinancialTransactionAndBankRecordsDuringYearAsync(accountId, transactionId, criterion.value.year);
            break;
        case "Month":
            dataStore.loadFinancialTransactionAndBankRecordsDuringMonthAsync(accountId, transactionId, criterion.value.year, criterion.value.month);
            break;
        case "Day":
            dataStore.loadFinancialTransactionAndBankRecordsDuringDayAsync(accountId, transactionId, criterion.value.year, criterion.value.month, criterion.value.day);
            break;
    }
}

</script>

<template>
    <DataMessages :retrievalStatus="dataStore.dataStatus"
    loadingMessage="Loading Transaction and Records..."
    errorMessage="Could Not Load Transaction or Records...">
        <h1 class="libre-baskerville-regular AttachRecordsView-header">{{ dataStore.data.financialTransaction.name }}</h1>
        <DateFilter @applyFilter="reloadData" class="AttachRecordsView-filter"/>
        <AttachBankRecords
            :financialTransaction="dataStore.data.financialTransaction"
            :bankRecords="dataStore.data.bankRecords"/>
    </DataMessages>
</template>

<style scoped>
.AttachRecordsView-header {
    text-align: center;
    text-decoration: underline;
    margin-bottom: 1rem;
}
.AttachRecordsView-filter {
    margin-bottom: 2rem;
}
</style>