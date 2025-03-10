<script setup>
/*
* BankRecordOrFinancialTransactionDetails represents either a
* BankRecordDetails or a FinancialTransactionDetails
* Props
* - data (BankRecordModel if isBankRecord, else FinancialTransactionModel)
* - isBankRecord (true if BankRecordDetails, false if FinancialTransactionDetails)
*/
import { useRouter } from 'vue-router';

import useDataStore from '@/store/DataStore';
import { formatCurrency } from '@/utilities/utilities';

import BankRecordItem from '../BankRecordOrFinancialTransactionItem/BankRecordItem.vue';
import FinancialTransactionItem from '../BankRecordOrFinancialTransactionItem/FinancialTransactionItem.vue';

const dataStore = useDataStore();
const router = useRouter();

const {data, isBankRecord} = defineProps(["data", "isBankRecord"]);

function editItem() {
    dataStore.expireData();
    const url = isBankRecord ? `/records/${data.id}/update` : `/transactions/${data.id}/update`;
    router.push(url).then(dataStore.resetData);
}

function goBack() {
    dataStore.expireData();
    const url = isBankRecord ? '/records' : '/transactions';
    router.push(url).then(dataStore.resetData);
}

function deleteItem() {
    if (isBankRecord)
        dataStore.deleteBankRecordAsync(data.id)
            .then(() => router.push("/records"))
            .then(dataStore.resetData);
    else
        dataStore.deleteFinancialTransactionAsync(data.id)
            .then(() => router.push("/transactions"))
            .then(dataStore.resetData);
}

function attachSubitems() {
    dataStore.expireData();
    if (isBankRecord)
        router.push(`/records/${data.id}/transactions`)
            .then(dataStore.resetData);
    else
        router.push(`/transactions/${data.id}/records`)
            .then(dataStore.resetData);
}

</script>

<template>
    <h1 class="happy-monkey-regular">{{ data.name }}</h1>
    <p class="ubuntu-regular">Date: {{ data.dateObj.format() }}</p>
    <p class="ubuntu-regular">Amount: {{ formatCurrency(data.amount) }}</p>
    <div class="BankRecordOrFinancialTransactionDetails-buttons">
        <button class="ubuntu-regular BankRecordOrFinancialTransactionDetails-edit-button" @click="editItem">Edit</button>
        <button class="ubuntu-regular" @click="goBack">Back</button>
        <button class="ubuntu-regular BankRecordOrFinancialTransactionDetails-delete-button" @click="deleteItem">Delete</button>
    </div>
    <div v-if="isBankRecord">
        <p class="ubuntu-regular">Transactions</p>
        <FinancialTransactionItem v-for="financialTransaction of data.financialTransactions" :key="financialTransaction.id" :recordId="data.id" :transaction="financialTransaction" :isAttached="true"/>
    </div>
    <div v-else>
        <p class="ubuntu-regular">Records</p>
        <BankRecordItem v-for="bankRecord of data.bankRecords" :key="bankRecord.id" :transactionId="data.id" :record="bankRecord" :isAttached="true"/>
    </div>
    <div class="BankRecordOrFinancialTransactionDetails-link-buttons">
        <button class="ubuntu-regular" @click="attachSubitems">Attach</button>
    </div>
</template>

<style scoped>
.BankRecordOrFinancialTransactionDetails-buttons button,
.BankRecordOrFinancialTransactionDetails-link-buttons button {
    background-color: white;
    font-size: 12pt;
    border-width: 0px;
}
.BankRecordOrFinancialTransactionDetails-buttons button:hover,
.BankRecordOrFinancialTransactionDetails-link-buttons button:hover {
    text-decoration: underline;
    cursor: pointer;
}
.BankRecordOrFinancialTransactionDetails-buttons {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
}
.BankRecordOrFinancialTransactionDetails-edit-button {
    color: #050;
}
.BankRecordOrFinancialTransactionDetails-delete-button {
    color: #c00;
}
.BankRecordOrFinancialTransactionDetails-link-buttons {
    display: flex;
    flex-direction: row;
    justify-content: center;
}
</style>