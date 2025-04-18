<script setup>
import { watch, ref } from 'vue';

import AttachFinancialTransactions from '@/components/AttachBankRecordsOrFinancialTransactions/AttachFinancialTransactions.vue';
import DataMessages from '@/components/DataMessages.vue';
import DateFilter from '@/components/DateFilter.vue';
import useDataStore from '@/store/DataStore';

const dataStore = useDataStore();

const criterionType = ref("None");
const criterion = ref(null);

const { recordId } = defineProps(["recordId"]);

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
            dataStore.loadBankRecordAndFinancialTransactions(recordId);
            break;
        case "Year":
            dataStore.loadBankRecordsAndFinancialTransactionsDuringDay(recordId, criterion.value.year);
            break;
        case "Month":
            dataStore.loadBankRecordAndFinancialTransactionsDuringMonth(recordId, criterion.value.year, criterion.value.month);
            break;
        case "Day":
            dataStore.loadBankRecordsAndFinancialTransactionsDuringDay(recordId, criterion.value.year, criterion.value.month, criterion.value.day);
            break;
    }
}

</script>

<template>
    <DataMessages :retrievalStatus="dataStore.retrievalStatus"
    loadingMessage="Loading Record and Transactions..."
    errorMessage="Could Not Load Record or Transactions...">
        <h1 class="libre-baskerville-regular AttachTransactionsView-header">{{ dataStore.data.bankRecord.name }}</h1>
        <DateFilter @applyFilter="reloadData" class="AttachTransactionsView-filter"/>
        <AttachFinancialTransactions
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