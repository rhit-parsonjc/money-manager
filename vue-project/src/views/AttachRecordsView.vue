<script setup>
import { watch, ref } from 'vue';

import AttachBankRecords from '@/components/AttachBankRecordsOrFinancialTransactions/AttachBankRecords.vue';
import DataMessages from '@/components/DataMessages.vue';
import DateFilter from '@/components/DateFilter.vue';
import useDataStore from '@/store/DataStore';

const dataStore = useDataStore();

const criterionType = ref("None");
const criterion = ref(null);

const { transactionId } = defineProps(["transactionId"]);

watch(() => dataStore.retrievalStatus,
    (newRetrievalStatus) => {
        if (newRetrievalStatus === 'NOT LOADED') {
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
            dataStore.loadFinancialTransactionAndBankRecords(transactionId);
            break;
        case "Year":
            dataStore.loadFinancialTransactionAndBankRecordsDuringYear(transactionId, criterion.value.year);
            break;
        case "Month":
            dataStore.loadFinancialTransactionAndBankRecordsDuringMonth(transactionId, criterion.value.year, criterion.value.month);
            break;
        case "Day":
            dataStore.loadFinancialTransactionAndBankRecordsDuringDay(transactionId, criterion.value.year, criterion.value.month, criterion.value.day);
            break;
    }
}

</script>

<template>
    <DateFilter @applyFilter="reloadData" />
    <DataMessages :retrievalStatus="dataStore.retrievalStatus"
    loadingMessage="Loading Transaction and Records..."
    errorMessage="Could Not Load Transaction or Records...">
        <AttachBankRecords
            :financialTransaction="dataStore.data.financialTransaction"
            :bankRecords="dataStore.data.bankRecords"/>
    </DataMessages>
</template>