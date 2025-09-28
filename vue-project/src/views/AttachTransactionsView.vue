<script setup>
import { watch, ref } from 'vue';
import { useRouter } from 'vue-router';

import AttachFinancialTransactions from '@/components/AttachRecordTransaction/AttachFinancialTransactions.vue';
import DataMessages from '@/components/DataMessages.vue';
import DateFilter from '@/components/DateFilter.vue';
import useDataStore, { DataStatus } from '@/store/DataStore';

const dataStore = useDataStore();
const router = useRouter();

const criterionType = ref("None");
const criterion = ref(null);

const { accountId, recordId } = defineProps(["accountId", "recordId"]);

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
            promise = dataStore.loadBankRecordAndFinancialTransactionsAsync(accountId, recordId);
            break;
        case "Year":
            promise = dataStore.loadBankRecordAndFinancialTransactionsDuringDayAsync(accountId, recordId, criterion.value.year);
            break;
        case "Month":
            promise = dataStore.loadBankRecordAndFinancialTransactionsDuringMonthAsync(accountId, recordId, criterion.value.year, criterion.value.month);
            break;
        case "Day":
            promise = dataStore.loadBankRecordAndFinancialTransactionsDuringDayAsync(accountId, recordId, criterion.value.year, criterion.value.month, criterion.value.day);
            break;
    }
    promise.catch(err => {
        if (err === 'Unauthorized') {
            router.push('/');
        }
    });
}

</script>

<template>
    <DataMessages :retrievalStatus="dataStore.dataStatus"
    loadingMessage="Loading Record and Transactions..."
    errorMessage="Could Not Load Record or Transactions...">
        <h1 class="libre-baskerville-regular AttachTransactionsView-header">{{ dataStore.data.bankRecord.name }}</h1>
        <DateFilter @applyFilter="reloadData" class="AttachTransactionsView-filter"/>
        <AttachFinancialTransactions
            :accountId="accountId"
            :bankRecord="dataStore.data.bankRecord"
            :financialTransactions="dataStore.data.financialTransactions"/>
    </DataMessages>
</template>

<style scoped>

.AttachTransactionsView-header {
    text-align: center;
    text-decoration: underline;
    margin-bottom: 1rem;
}
.AttachTransactionsView-filter {
    margin-bottom: 2rem;
}
</style>