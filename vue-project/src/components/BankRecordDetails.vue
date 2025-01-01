<script setup>
const {record} = defineProps(["record"])
import useDataStore from '@/store/DataStore';
import { formatCurrency } from '@/utilities/utilities';
import { useRouter } from 'vue-router';

const dataStore = useDataStore();
const router = useRouter();

function deleteRecord() {
    dataStore.deleteRecord(record.id);
    router.push("/records");
}

</script>

<template>
    <h1 class="happy-monkey-regular">{{ record.name }}</h1>
    <p class="libre-baskerville-regular">Date: {{ record.dateObj.format() }}</p>
    <p class="libre-baskerville-regular">Amount: {{ formatCurrency(record.amount) }}</p>
    <div id="bankRecordButtons">
        <button class="libre-baskerville-regular" id="saveButton">Save</button>
        <button class="libre-baskerville-regular" id="backButton">Back</button>
        <button class="libre-baskerville-regular" id="deleteButton" @click="deleteRecord">Delete</button>
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
#saveButton {
    color: #050;
}
#deleteButton {
    color: #c00;
}
</style>