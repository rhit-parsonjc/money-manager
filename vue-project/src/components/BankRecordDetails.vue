<script setup>
const {record} = defineProps(["record"])
import useDataStore from '@/store/DataStore';
import { useRouter } from 'vue-router';
import { computed, ref } from 'vue';

const dataStore = useDataStore();
const router = useRouter();

function returnToRecords() {
    router.push("/records");
}

function saveRecord() {
    dataStore.updateRecord(record.id, {
        year: yearValue.value,
        month: monthNames.indexOf(monthNameValue.value) + 1,
        day: dayValue.value,
        amount: amountValue.value,
        name: nameValue.value,
    })
    returnToRecords()
}

function deleteRecord() {
    dataStore.deleteRecord(record.id);
    returnToRecords();
}

const monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"]

let monthNameValue = ref(monthNames[record.dateObj.monthValue - 1])
let dayValue = ref(record.dateObj.dayValue)
let yearValue = ref(record.dateObj.yearValue)
let amountValue = ref(record.amount.toFixed(2))
let nameValue = ref(record.name)

const daysInMonth = computed(() => {
    const isLeapYear = (yearValue.value % 400 === 0) || (yearValue.value % 100 !== 0 && yearValue.value % 4 === 0)
    const daysPerMonth = {
        "January": 31,
        "February": (isLeapYear ? 29 : 28),
        "March": 31,
        "April": 30,
        "May": 30,
        "June": 31,
        "July": 31,
        "August": 31,
        "September": 30,
        "October": 31,
        "November": 30,
        "December": 31
    }
    return daysPerMonth[monthNameValue.value]
})

</script>

<template>
    <input class="happy-monkey-regular nameInput" v-model="nameValue">
    <div class="inputLine">
        <p class="libre-baskerville-regular">Date:</p>
        <select class="libre-baskerville-regular" v-model="monthNameValue">
            <option v-for="monthName of monthNames" :key="monthName">{{ monthName }}</option>
        </select>
        <input class="libre-baskerville-regular" type="number" v-model="dayValue" min="1" :max="daysInMonth">
        <input class="libre-baskerville-regular" type="number" v-model="yearValue">
    </div>
    <div class="inputLine">
        <p class="libre-baskerville-regular">Amount: $</p>
        <input class="libre-baskerville-regular" v-model="amountValue">
    </div>
    <div id="bankRecordButtons">
        <button class="libre-baskerville-regular" id="saveButton" @click="saveRecord">Save</button>
        <button class="libre-baskerville-regular" id="backButton" @click="returnToRecords">Back</button>
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
.inputLine {
    display: flex;
    flex-direction: row;
}
.nameInput {
    font-size: 18pt;
}
</style>