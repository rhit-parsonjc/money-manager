<script setup>
const {record} = defineProps(["record"])
import useDataStore from '@/store/DataStore';
import { formatCurrency } from '@/utilities/utilities';
import { useRouter } from 'vue-router';

const dataStore = useDataStore();
const router = useRouter();

function editRecord() {
    router.push(`/records/${record.id}/update`)
}

function returnToRecords() {
    router.push("/records");
}

function deleteRecord() {
    dataStore.deleteRecord(record.id);
    returnToRecords();
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