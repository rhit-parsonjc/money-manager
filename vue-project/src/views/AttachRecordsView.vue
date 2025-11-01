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
    <div class="container-fluid p-3">
        <DataMessages :retrievalStatus="dataStore.dataStatus"
        loadingMessage="Loading Transaction and Records..."
        errorMessage="Could Not Load Transaction or Records...">
        <div class="row m-0 mb-3">
            <h1 class="libre-baskerville-regular text-center p-0">{{ dataStore.data.financialTransaction.name }}</h1>
        </div>
        <div class="row justify-content-center m-0 mb-5">
            <div class="col-sm-5 col-md-4 p-0">
                <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToTransactionPage">View Transaction</a>
            </div>
        </div>
        <DateFilter @applyFilter="reloadData" class="mb-3"/>
        <AttachBankRecords
            :accountId="accountId"
            :financialTransaction="dataStore.data.financialTransaction"
            :bankRecords="dataStore.data.bankRecords"/>
        </DataMessages>
    </div>
</template>