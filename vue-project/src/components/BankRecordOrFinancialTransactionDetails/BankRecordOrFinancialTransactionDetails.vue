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
</template>

<style scoped>
.BankRecordOrFinancialTransactionDetails-buttons button {
    background-color: white;
    font-size: 12pt;
    border-width: 0px;
}
.BankRecordOrFinancialTransactionDetails-buttons button:hover {
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
</style>