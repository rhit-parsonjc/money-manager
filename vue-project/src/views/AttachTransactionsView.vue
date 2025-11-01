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

function goToRecordPage() {
    dataStore.expireData();
    router.push(`/accounts/${accountId}/records/${recordId}`).then(dataStore.resetData);
}

</script>

<template>
    <div class="container-fluid p-3">
        <DataMessages :retrievalStatus="dataStore.dataStatus"
        loadingMessage="Loading Record and Transactions..."
        errorMessage="Could Not Load Record or Transactions...">
            <div class="row m-0 mb-3">
                <h1 class="libre-baskerville-regular text-center p-0">{{ dataStore.data.bankRecord.name }}</h1>
            </div>
            <div class="row justify-content-center m-0 mb-5">
                <div class="col-sm-4 col-md-3 p-0">
                    <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToRecordPage">View Record</a>
                </div>
            </div>
            <DateFilter @applyFilter="reloadData" class="mb-3"/>
            <AttachFinancialTransactions
                :accountId="accountId"
                :bankRecord="dataStore.data.bankRecord"
                :financialTransactions="dataStore.data.financialTransactions"/>
        </DataMessages>
    </div>
</template>