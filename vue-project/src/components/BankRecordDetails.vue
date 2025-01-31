<script setup>
/**
 * BankRecordDetails displays in-depth information about a single bank record.
 * Props:
 * - record (type BankRecordModel)
 */
 
import { useRouter } from 'vue-router'
import useDataStore from '@/store/DataStore'
import { formatCurrency } from '@/utilities/utilities'

const {record} = defineProps(["record"])

const dataStore = useDataStore()
const router = useRouter()

function editRecord() {
    dataStore.expireData()
    router.push(`/records/${record.id}/update`).then(dataStore.resetData)
}

function returnToRecords() {
    dataStore.expireData()
    router.push("/records").then(dataStore.resetData)
}

function deleteRecord() {
    dataStore.deleteBankRecordAsync(record.id)
        .then(() => router.push("/records"))
        .then(dataStore.resetData)
}

</script>

<template>
    <h1 class="happy-monkey-regular">{{ record.name }}</h1>
    <p class="ubuntu-regular">Date: {{ record.dateObj.format() }}</p>
    <p class="ubuntu-regular">Amount: {{ formatCurrency(record.amount) }}</p>
    <div id="bankRecordButtons">
        <button class="ubuntu-regular" id="editButton" @click="editRecord">Edit</button>
        <button class="ubuntu-regular" id="backButton" @click="returnToRecords">Back</button>
        <button class="ubuntu-regular" id="deleteButton" @click="deleteRecord">Delete</button>
    </div>
</template>

<style scoped>
#bankRecordButtons button {
    background-color: white;
    font-size: 12pt;
    border-width: 0px;
}
#bankRecordButtons button:hover {
    text-decoration: underline;
    cursor: pointer;
}
#bankRecordButtons {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
}
#editButton {
    color: #050;
}
#deleteButton {
    color: #c00;
}
</style>