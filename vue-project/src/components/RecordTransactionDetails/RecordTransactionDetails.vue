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

function goToEditItemPage() {
    dataStore.expireData();
    const url = `/accounts/${accountId}` + (isBankRecord ? `/records/${data.id}/update` : `/transactions/${data.id}/update`);
    router.push(url).then(dataStore.resetData);
}

function goToListPage() {
    dataStore.expireData();
    const url = `/accounts/${accountId}` + (isBankRecord ? '/records' : '/transactions');
    router.push(url).then(dataStore.resetData);
}
/*
function deleteItem() {
    if (isBankRecord)
        dataStore.deleteBankRecordAsync(accountId, data.id)
            .then(() => router.push(`/accounts/${accountId}/records`))
            .then(dataStore.resetData)
            .catch(err => {
                if (err === 'Unauthorized') {
                    router.push('/');
                }
            });
    else
        dataStore.deleteFinancialTransactionAsync(accountId, data.id)
            .then(() => router.push(`/accounts/${accountId}/transactions`))
            .then(dataStore.resetData)
            .catch(err => {
                if (err === 'Unauthorized') {
                    router.push('/');
                }
            });
}
*/

function goToAttachSubitemsPage() {
    dataStore.expireData();
    if (isBankRecord)
        router.push(`/accounts/${accountId}/records/${data.id}/transactions`)
            .then(dataStore.resetData)
            .catch(err => {
                if (err === 'Unauthorized') {
                    router.push('/');
                }
            });
    else
        router.push(`/accounts/${accountId}/transactions/${data.id}/records`)
            .then(dataStore.resetData)
            .catch(err => {
                if (err === 'Unauthorized') {
                    router.push('/');
                }
            });
}

function uploadFile() {
    const files = document.getElementById("RecordTransactionDetails-file-input").files;
    if (files.length !== 1) {
        return;
    }
    const file = files[0];
    if (isBankRecord)
        dataStore.attachFileToItemAsync(file, data.id)
            .then(dataStore.resetData)
            .catch(err => {
                if (err === 'Unauthorized') {
                    router.push('/');
                }
            });
    else
        dataStore.attachFileToItemAsync(file, data.id)
            .then(dataStore.resetData)
            .catch(err => {
                if (err === 'Unauthorized') {
                    router.push('/');
                }
            });
}

function loadFile(id, name) {
    dataStore.loadFileAttachmentAsync(id)
    .then((result) => {
        const fileURL = window.URL.createObjectURL(result);
        const a = document.createElement('a');
        a.href = fileURL;
        a.download = name;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(fileURL);
    })
    .catch(err => {
        if (err === 'Unauthorized') {
            router.push('/');
        }
    });
}

/*
function deleteFile(id) {
    dataStore.deleteFileAttachmentAsync(id)
        .then(dataStore.resetData)
        .catch(err => {
            if (err === 'Unauthorized') {
                router.push('/');
            }
        });
}
*/

</script>

<template>
    <div class="row m-0">
        <p class="ubuntu-regular"><span class="fw-bold">Name:</span> {{ data.name }}</p>
    </div>
    <div class="row m-0">
        <p class="ubuntu-regular"><span class="fw-bold">Date:</span> {{ data.dateObj.format() }}</p>
    </div>
    <div class="row m-0">
        <p class="ubuntu-regular"><span class="fw-bold">Amount:</span> {{ formatCurrency(data.amount) }}</p>
    </div>
    <div class="row m-0 justify-content-start mb-5">
        <div class="col-sm-5 col-md-3 p-0 me-sm-5 mb-3 mb-sm-0">
            <button class="btn btn-primary btn-lg happy-monkey-regular" @click="goToEditItemPage">Edit {{ isBankRecord ? "Record" : "Transaction" }}</button>
        </div>
        <div class="col-sm-5 col-md-3 p-0">
            <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToListPage">View {{ isBankRecord ? "Records" : "Transactions" }}</a>
        </div>
    </div>
    <div class="row m-0 mb-2">
        <h2 class="libre-baskerville-regular text-center p-0">{{ isBankRecord ? "Transactions" : "Records" }}</h2>
    </div>
    <div class="row m-0" v-if="isBankRecord">
        <FinancialTransactionItem v-for="financialTransaction of data.financialTransactions" :key="financialTransaction.id" :accountId="accountId" :recordId="data.id" :transaction="financialTransaction" :isAttached="true" class="mb-2"/>
    </div>
    <div class="row m-0" v-else>
        <BankRecordItem v-for="bankRecord of data.bankRecords" :key="bankRecord.id" :accountId="accountId" :transactionId="data.id" :record="bankRecord" :isAttached="true" class="mb-2"/>
    </div>
    <div class="row justify-content-center m-0 mb-5">
        <div class="col-sm-8 col-md-6 p-0">
            <button class="btn btn-primary btn-lg happy-monkey-regular" @click="goToAttachSubitemsPage">Attach/Detach {{ isBankRecord ? "Transactions" : "Records" }}</button>
        </div>
    </div>
    <div class="row m-0 mb-2">
        <h2 class="libre-baskerville-regular text-center p-0">Files</h2>
    </div>
    <div class="row m-0 mb-3">
        <ul id="RecordTransactionDetails-files" class="p-0 m-0">
            <li v-for="fileAttachment of data.fileAttachments" :key="fileAttachment.id" class="RecordTransactionDetails-file-line">
                <a class="happy-monkey-regular" @click="loadFile(fileAttachment.id, fileAttachment.name)">{{ fileAttachment.name }}</a>
            </li>
        </ul>
    </div>
    <div class="row m-0 justify-content-between">
        <div class="col-sm-7 col-lg-9 p-0 mb-2 mb-sm-0">
            <input type="file" class="ubuntu-regular">
        </div>
        <div class="col-sm-4 col-lg-2 p-0">
            <button class="btn btn-secondary btn-lg happy-monkey-regular" @click="uploadFile">Upload File</button>
        </div>
    </div>
</template>

<style scoped>
#RecordTransactionDetails-files {
    list-style-type: none;
}
</style>