<script setup>
/**
 * BankRecordDetails displays in-depth information about a single bank record.
 * Props:
 * - record (type BankRecordModel)
 */
 
import { useRouter } from 'vue-router';

import useDataStore from '@/store/DataStore';
import { formatCurrency } from '@/utilities/utilities';

const dataStore = useDataStore();
const router = useRouter();

const {record} = defineProps(["record"]);

function editRecord() {
    dataStore.expireData();
    router.push(`/records/${record.id}/update`).then(dataStore.resetData);
}

function returnToRecords() {
    dataStore.expireData();
    router.push("/records").then(dataStore.resetData);
}

function deleteRecord() {
    dataStore.deleteBankRecordAsync(record.id)
        .then(() => router.push("/records"))
        .then(dataStore.resetData);
}

</script>

<template>
    <h1 class="happy-monkey-regular">{{ record.name }}</h1>
    <p class="ubuntu-regular">Date: {{ record.dateObj.format() }}</p>
    <p class="ubuntu-regular">Amount: {{ formatCurrency(record.amount) }}</p>
    <div class="BankRecordDetails-buttons">
        <button class="ubuntu-regular BankRecordDetails-edit-button" @click="editRecord">Edit</button>
        <button class="ubuntu-regular" @click="returnToRecords">Back</button>
        <button class="ubuntu-regular BankRecordDetails-delete-button" @click="deleteRecord">Delete</button>
    </div>
</template>

<style scoped>
.BankRecordDetails-buttons button {
    background-color: white;
    font-size: 12pt;
    border-width: 0px;
}
.BankRecordDetails-buttons button:hover {
    text-decoration: underline;
    cursor: pointer;
}
.BankRecordDetails-buttons {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
}
.BankRecordDetails-edit-button {
    color: #050;
}
.BankRecordDetails-delete-button {
    color: #c00;
}
</style>