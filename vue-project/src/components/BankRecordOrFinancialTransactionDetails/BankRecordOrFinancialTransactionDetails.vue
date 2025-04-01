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

function uploadFile() {
    const files = document.getElementById("BankRecordOrFinancialTransactionDetails-file-input").files;
    if (files.length !== 1) {
        return;
    }
    const file = files[0];
    if (isBankRecord)
        dataStore.attachFileToBankRecordAsync(file, data.id)
            .then(dataStore.resetData);
    else
        dataStore.attachFileToFinancialTransactionAsync(file, data.id)
            .then(dataStore.resetData);
}

function deleteFile(id) {
    dataStore.deleteFileAttachmentAsync(id)
        .then(dataStore.resetData);
}

</script>

<template>
    <p class="happy-monkey-regular">Name: {{ data.name }}</p>
    <p class="happy-monkey-regular">Date: {{ data.dateObj.format() }}</p>
    <p class="happy-monkey-regular">Amount: {{ formatCurrency(data.amount) }}</p>
    <div class="BankRecordOrFinancialTransactionDetails-buttons">
        <button class="ubuntu-regular BankRecordOrFinancialTransactionDetails-edit-button" @click="editItem">Edit</button>
        <button class="ubuntu-regular" @click="goBack">Back</button>
        <button class="ubuntu-regular BankRecordOrFinancialTransactionDetails-delete-button" @click="deleteItem">Delete</button>
    </div>
    <div v-if="isBankRecord">
        <h2 class="happy-monkey-regular">Financial Transactions</h2>
        <FinancialTransactionItem v-for="financialTransaction of data.financialTransactions" :key="financialTransaction.id" :recordId="data.id" :transaction="financialTransaction" :isAttached="true" class="BankRecordOrFinancialTransactionDetails-item"/>
    </div>
    <div v-else>
        <h2 class="happy-monkey-regular">Bank Records</h2>
        <BankRecordItem v-for="bankRecord of data.bankRecords" :key="bankRecord.id" :transactionId="data.id" :record="bankRecord" :isAttached="true" class="BankRecordOrFinancialTransactionDetails-item"/>
    </div>
    <div class="BankRecordOrFinancialTransactionDetails-link-buttons">
        <button class="ubuntu-regular" @click="attachSubitems">Attach</button>
    </div>
    <h2 class="happy-monkey-regular">Attached Files</h2>
    <ul class="BankRecordOrFinancialTransactionsDetails-files">
        <li v-for="fileAttachment of data.fileAttachments" :key="fileAttachment.id" class="BankRecordOrFinancialTransactionDetails-file-line">
            <a :href="`http://localhost:8080/api/v1/fileattachments/${fileAttachment.id}`" class="happy-monkey-regular">{{ fileAttachment.name }}</a>
            <button class="ubuntu-regular BankRecordOrFinancialTransactionDetails-delete-file-button" @click="deleteFile(fileAttachment.id)">Delete</button>
        </li>
    </ul>
    <input type="file" class="ubuntu-regular" id="BankRecordOrFinancialTransactionDetails-file-input">
    <button class="ubuntu-regular" @click="uploadFile">Upload</button>
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
}
.BankRecordOrFinancialTransactionDetails-buttons {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    margin-bottom: 2rem;
    margin-top: 0.5rem;
}
.BankRecordOrFinancialTransactionDetails-item {
    margin-bottom: 1rem;
    margin-left: 1rem;
}
.BankRecordOrFinancialTransactionDetails-item:last-child {
    margin-bottom: 0rem;
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
    margin-bottom: 2rem;
    margin-top: 1rem;
}
.BankRecordOrFinancialTransactionsDetails-files {
    margin: 0.5em 0em;
    list-style-type: none;
    padding-left: 0.5em;
}
.BankRecordOrFinancialTransactionDetails-file-line {
    display: flex;
    flex-direction: row;
    align-items: center;
}
.BankRecordOrFinancialTransactionDetails-delete-file-button {
    margin-left: 1em;
}
</style>