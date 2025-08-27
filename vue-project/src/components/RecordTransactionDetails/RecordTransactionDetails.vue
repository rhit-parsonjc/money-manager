<script setup>
/*
* BankRecordOrFinancialTransactionDetails represents either a
* BankRecordDetails or a FinancialTransactionDetails
*/
import { useRouter } from 'vue-router';

import useDataStore from '@/store/DataStore';
import { formatCurrency } from '@/utilities/utilities';

import BankRecordItem from '../RecordTransactionItem/BankRecordItem.vue';
import FinancialTransactionItem from '../RecordTransactionItem/FinancialTransactionItem.vue';

const dataStore = useDataStore();
const router = useRouter();

const { accountId, data, isBankRecord } = defineProps(["accountId", "data", "isBankRecord"]);

function editItem() {
    dataStore.expireData();
    const url = `/accounts/${accountId}` + (isBankRecord ? `/records/${data.id}/update` : `/transactions/${data.id}/update`);
    router.push(url).then(dataStore.resetData);
}

function goBack() {
    dataStore.expireData();
    const url = `/accounts/${accountId}` + (isBankRecord ? '/records' : '/transactions');
    router.push(url).then(dataStore.resetData);
}

function deleteItem() {
    if (isBankRecord)
        dataStore.deleteBankRecordAsync(accountId, data.id)
            .then(() => router.push(`/accounts/${accountId}/records`))
            .then(dataStore.resetData);
    else
        dataStore.deleteFinancialTransactionAsync(accountId, data.id)
            .then(() => router.push(`/accounts/${accountId}/transactions`))
            .then(dataStore.resetData);
}

function attachSubitems() {
    dataStore.expireData();
    if (isBankRecord)
        router.push(`/accounts/${accountId}/records/${data.id}/transactions`)
            .then(dataStore.resetData);
    else
        router.push(`/accounts/${accountId}/transactions/${data.id}/records`)
            .then(dataStore.resetData);
}

function uploadFile() {
    const files = document.getElementById("RecordTransactionDetails-file-input").files;
    if (files.length !== 1) {
        return;
    }
    const file = files[0];
    if (isBankRecord)
        dataStore.attachFileToItemAsync(file, data.id)
            .then(dataStore.resetData);
    else
        dataStore.attachFileToItemAsync(file, data.id)
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
    <div class="RecordTransactionDetails-buttons">
        <button class="ubuntu-regular RecordTransactionDetails-edit-button" @click="editItem">Edit</button>
        <button class="ubuntu-regular" @click="goBack">Back</button>
        <button class="ubuntu-regular RecordTransactionDetails-delete-button" @click="deleteItem">Delete</button>
    </div>
    <div v-if="isBankRecord">
        <h2 class="happy-monkey-regular">Financial Transactions</h2>
        <FinancialTransactionItem v-for="financialTransaction of data.financialTransactions" :key="financialTransaction.id" :recordId="data.id" :transaction="financialTransaction" :isAttached="true" class="RecordTransactionDetails-item"/>
    </div>
    <div v-else>
        <h2 class="happy-monkey-regular">Bank Records</h2>
        <BankRecordItem v-for="bankRecord of data.bankRecords" :key="bankRecord.id" :transactionId="data.id" :record="bankRecord" :isAttached="true" class="RecordTransactionDetails-item"/>
    </div>
    <div class="RecordTransactionDetails-link-buttons">
        <button class="ubuntu-regular" @click="attachSubitems">Attach</button>
    </div>
    <h2 class="happy-monkey-regular">Attached Files</h2>
    <ul class="RecordTransactionDetails-files">
        <li v-for="fileAttachment of data.fileAttachments" :key="fileAttachment.id" class="RecordTransactionDetails-file-line">
            <a :href="`http://localhost:8080/api/v1/fileattachments/${fileAttachment.id}`" class="happy-monkey-regular">{{ fileAttachment.name }}</a>
            <button class="ubuntu-regular RecordTransactionDetails-delete-file-button" @click="deleteFile(fileAttachment.id)">Delete</button>
        </li>
    </ul>
    <input type="file" class="ubuntu-regular" id="RecordTransactionDetails-file-input">
    <button class="ubuntu-regular" @click="uploadFile">Upload</button>
</template>

<style scoped>
.RecordTransactionDetails-buttons button,
.RecordTransactionDetails-link-buttons button {
    background-color: white;
    font-size: 12pt;
    border-width: 0px;
}
.RecordTransactionDetails-buttons button:hover,
.RecordTransactionDetails-link-buttons button:hover {
    text-decoration: underline;
}
.RecordTransactionDetails-buttons {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    margin-bottom: 2rem;
    margin-top: 0.5rem;
}
.RecordTransactionDetails-item {
    margin-bottom: 1rem;
    margin-left: 1rem;
}
.RecordTransactionDetails-item:last-child {
    margin-bottom: 0rem;
}
.RecordTransactionDetails-edit-button {
    color: #050;
}
.RecordTransactionDetails-delete-button {
    color: #c00;
}
.RecordTransactionDetails-link-buttons {
    display: flex;
    flex-direction: row;
    justify-content: center;
    margin-bottom: 2rem;
    margin-top: 1rem;
}
.RecordTransactionDetails-files {
    margin: 0.5em 0em;
    list-style-type: none;
    padding-left: 0.5em;
}
.RecordTransactionDetails-file-line {
    display: flex;
    flex-direction: row;
    align-items: center;
}
.RecordTransactionDetails-delete-file-button {
    margin-left: 1em;
}
</style>