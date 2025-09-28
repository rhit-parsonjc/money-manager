<script setup>
import { watch, ref } from 'vue';
import { useRouter } from 'vue-router';

import DataMessages from '@/components/DataMessages.vue';
import useDataStore, { DataStatus } from '@/store/DataStore';
import DateItemAndTransactionsList from '@/components/DateItemList/DateItemAndTransactionsList.vue';
import DateFilter from '@/components/DateFilter.vue';

const dataStore = useDataStore();
const router = useRouter();

const { accountId } = defineProps(["accountId"]);

const criterionType = ref("None");
const criterion = ref(null);

watch(() => dataStore.dataStatus,
    (newDataStatus) => {
        if (newDataStatus === DataStatus.NOT_LOADED) {
            loadData();
        }
    },
    {immediate: true}
);

function goToCreateTransactionPage() {
    dataStore.expireData();
    router.push(`/accounts/${accountId}/create/transaction`).then(dataStore.resetData);
}

function reloadData(criterionInfo) {
    criterionType.value = criterionInfo.criterionType;
    criterion.value = criterionInfo.criterion;
    dataStore.resetData();
}

function loadData() {
    let promise = null;
    switch (criterionType.value) {
        case "None":
            promise = dataStore.loadFinancialTransactionsAsync(accountId);
            break;
        case "Year":
            promise = dataStore.loadFinancialTransactionsDuringYearAsync(accountId, criterion.value.year);
            break;
        case "Month":
            promise = dataStore.loadFinancialTransactionsDuringMonthAsync(accountId, criterion.value.year, criterion.value.month);
            break;
        case "Day":
            promise = dataStore.loadFinancialTransactionsDuringDayAsync(accountId, criterion.value.year, criterion.value.month, criterion.value.day);
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
    <h1 class="libre-baskerville-regular FinancialTransactionsView-header">Financial Transactions</h1>
    <a @click="goToCreateTransactionPage" class="ubuntu-regular FinancialTransactionsView-add-button">Add New Financial Transaction</a>
    <DateFilter @applyFilter="reloadData" />
    <DataMessages :retrievalStatus="dataStore.dataStatus"
        loadingMessage="Loading Transactions..." errorMessage="Could Not Load Transactions">
        <DateItemAndTransactionsList
            :accountId="accountId"
            :financialTransactions="dataStore.data.financialTransactions"
            :criterionType="criterionType"
            :criterion="criterion"
        />
    </DataMessages>
</template>

<style scoped>
.FinancialTransactionsView-header {
    text-align: center;
    text-decoration: underline;
    margin-bottom: 1rem;
}
.FinancialTransactionsView-add-button {
    text-decoration: none;
    color: #055;
    display: block;
    text-align: center;
    margin-bottom: 0.5rem;
}
</style>