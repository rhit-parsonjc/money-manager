<script setup>
/*
* BankRecordOrFinancialTransaction represents either a BankRecord
* or a FinancialTransaction
* Props
* - data (BankRecordModel if isBankRecord, else FinancialTransactionModel)
* - isBankRecord (true if BankRecord, false if FinancialTransaction)
*/
import { useRouter } from 'vue-router';

import useDataStore from '@/store/DataStore';
import { formatCurrency } from '@/utilities/utilities';

const router = useRouter();
const dataStore = useDataStore();

const {data, isBankRecord} = defineProps(["data", "isBankRecord"]);

function goToDetailPage() {
    dataStore.expireData();
    const urlPage = isBankRecord ? `/records/${data.id}` : `/transactions/${data.id}`
    router.push(urlPage).then(dataStore.resetData);
}
</script>

<template>
    <li class="BankRecordOrFinancialTransaction">
        <a @click="goToDetailPage" class="happy-monkey-regular BankRecordOrFinancialTransaction-text">
            {{ data.name }} ({{ formatCurrency(data.amount) }})
        </a>
    </li>
</template>

<style scoped>
.BankRecordOrFinancialTransaction {
    margin: 0.25rem 0em;
}
.BankRecordOrFinancialTransaction-text {
    font-size: 12pt;
    color: black;
    text-decoration: none;
}
.BankRecordOrFinancialTransaction-text:hover {
    text-decoration: underline;
}
</style>