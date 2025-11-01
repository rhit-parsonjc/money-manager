<script setup>
import { watch, ref } from 'vue';
import { useRouter } from 'vue-router';

import DataMessages from '@/components/DataMessages.vue';
import useDataStore, { DataStatus } from '@/store/DataStore';
import DateItemAndRecordsList from '@/components/DateItemList/DateItemAndRecordsList.vue';
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

function goToCreateRecordPage() {
    dataStore.expireData();
    router.push(`/accounts/${accountId}/create/record`).then(dataStore.resetData);
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
            promise = dataStore.loadBankRecordsAndDateAmountsAsync(accountId);
            break;
        case "Year":
            promise = dataStore.loadBankRecordsAndDateAmountsDuringYearAsync(accountId, criterion.value.year);
            break;
        case "Month":
            promise = dataStore.loadBankRecordsAndDateAmountsDuringMonthAsync(accountId, criterion.value.year, criterion.value.month);
            break;
        case "Day":
            promise = dataStore.loadBankRecordsAndDateAmountsDuringDayAsync(accountId, criterion.value.year, criterion.value.month, criterion.value.day);
            break;
    }
    promise.catch(err => {
        if (err === 'Unauthorized') {
            router.push('/');
        }
    });
}

function goToTransactionsPage() {
    dataStore.expireData();
    router.push(`/accounts/${accountId}/transactions`).then(dataStore.resetData);
}

function goToAccountsPage() {
    dataStore.expireData();
    router.push(`/accounts/${accountId}`).then(dataStore.resetData);
}

</script>

<template>
    <div class="container-fluid p-3">
        <div class="row m-0 mb-3">
            <h1 class="libre-baskerville-regular text-center p-0">Records</h1>
        </div>
        <div class="row justify-content-around m-0 mb-4">
            <div class="col-sm-5 col-md-4 p-0 mb-3">
                <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToAccountsPage">View Account</a>
            </div>
            <div class="col-sm-5 col-md-4 p-0">
                <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToTransactionsPage">View Transactions</a>
            </div>
        </div>
        <div class="row m-0 justify-content-center mb-3">
            <div class="col-sm-7 col-md-5 p-0">
                <button class="btn btn-primary btn-lg happy-monkey-regular" @click="goToCreateRecordPage">Create Record</button>
            </div>
        </div>
        <div class="row m-0">
            <DateFilter @applyFilter="reloadData" />
        </div>
        <div class="row m-0">
            <DataMessages :retrievalStatus="dataStore.dataStatus"
                loadingMessage="Loading Records..." errorMessage="Could Not Load Records">
                <DateItemAndRecordsList
                    :accountId="accountId"
                    :bankRecords="dataStore.data.bankRecords"
                    :dateAmounts="dataStore.data.dateAmounts"
                    :criterionType="criterionType"
                    :criterion="criterion"
                />
            </DataMessages>
        </div>
    </div>
</template>