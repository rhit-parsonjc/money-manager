<script setup>
import { watch, ref } from 'vue';
import { useRouter } from 'vue-router';

import AttachBankRecords from '@/components/AttachRecordTransaction/AttachBankRecords.vue';
import DataMessages from '@/components/DataMessages.vue';
import DateFilter from '@/components/DateFilter.vue';
import useDataStore, { DataStatus } from '@/store/DataStore';

const dataStore = useDataStore();
const router = useRouter();

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
    let promise = null;
    switch (criterionType.value) {
        case "None":
            promise = dataStore.loadFinancialTransactionAndBankRecordsAsync(accountId, transactionId);
            break;
        case "Year":
            promise = dataStore.loadFinancialTransactionAndBankRecordsDuringYearAsync(accountId, transactionId, criterion.value.year);
            break;
        case "Month":
            promise = dataStore.loadFinancialTransactionAndBankRecordsDuringMonthAsync(accountId, transactionId, criterion.value.year, criterion.value.month);
            break;
        case "Day":
            promise = dataStore.loadFinancialTransactionAndBankRecordsDuringDayAsync(accountId, transactionId, criterion.value.year, criterion.value.month, criterion.value.day);
            break;
    }
    promise.catch(err => {
        if (err === 'Unauthorized') {
            router.push('/');
        }
    });
}

function goToTransactionPage() {
    dataStore.expireData();
    router.push(`/accounts/${accountId}/transactions/${transactionId}`).then(dataStore.resetData);
}

</script>

<template>
    <DataMessages :retrievalStatus="dataStore.dataStatus"
    loadingMessage="Loading Transaction and Records..."
    errorMessage="Could Not Load Transaction or Records...">
        <h1 class="libre-baskerville-regular AttachRecordsView-header">{{ dataStore.data.financialTransaction.name }}</h1>
        <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToTransactionPage">View Transaction</a>
        <DateFilter @applyFilter="reloadData" class="AttachRecordsView-filter"/>
        <AttachBankRecords
            :accountId="accountId"
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