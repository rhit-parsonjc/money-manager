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
    switch (criterionType.value) {
        case "None":
            dataStore.loadBankRecordsAndDateAmountsAsync(accountId);
            break;
        case "Year":
            dataStore.loadBankRecordsAndDateAmountsDuringYearAsync(accountId, criterion.value.year);
            break;
        case "Month":
            dataStore.loadBankRecordsAndDateAmountsDuringMonthAsync(accountId, criterion.value.year, criterion.value.month);
            break;
        case "Day":
            dataStore.loadBankRecordsAndDateAmountsDuringDayAsync(accountId, criterion.value.year, criterion.value.month, criterion.value.day);
            break;
    }
}

</script>

<template>
    <h1 class="libre-baskerville-regular BankRecordsView-header">Bank Records</h1>
    <a @click="goToCreateRecordPage" class="ubuntu-regular BankRecordsView-add-record">Add New Bank Record</a>
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
.BankRecordsView-header {
    text-align: center;
    text-decoration: underline;
    margin-bottom: 1rem;
}
.BankRecordsView-add-record {
    text-decoration: none;
    color: #050;
    display: block;
    text-align: center;
    margin-bottom: 0.5rem;
}
</style>