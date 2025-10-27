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

</script>

<template>
    <h1 class="libre-baskerville-regular BankRecordsView-header">Records</h1>
    <div id="BankRecordsView-create-button">
        <button class="btn btn-primary btn-lg happy-monkey-regular" @click="goToCreateRecordPage">Create Record</button>
    </div>
    <DateFilter @applyFilter="reloadData" />
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
</template>

<style scoped>
#BankRecordsView-create-button {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 0.5em;
}
</style>