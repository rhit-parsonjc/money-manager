<script setup>
import useDataStore from '@/store/DataStore';
import { useRouter } from 'vue-router';
import { computed, ref } from 'vue';

const {record} = defineProps(["record"])

const dataStore = useDataStore();
const router = useRouter();

const monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"]
const initialValues = (record === null) ? {
    monthValue: 1,
    dayValue: 1,
    yearValue: 0,
    amountValue: 0,
    nameValue: "",
} : {
    monthValue: record.dateObj.monthValue,
    dayValue: record.dateObj.dayValue,
    yearValue: record.dateObj.yearValue,
    amountValue: record.amount.toFixed(2),
    nameValue: record.name,
}

let monthNameValue = ref(monthNames[initialValues.monthValue - 1])
let dayValue = ref(initialValues.dayValue)
let yearValue = ref(initialValues.yearValue)
let amountValue = ref(initialValues.amountValue)
let nameValue = ref(initialValues.nameValue)

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

function createNewRecord() {
    return {
        year: yearValue.value,
        month: monthNames.indexOf(monthNameValue.value) + 1,
        day: dayValue.value,
        amount: amountValue.value,
        name: nameValue.value,
    }
}

function returnToRecord() {
    router.push(`/records/${record.id}`)
}

function returnToRecords() {
    router.push("/records/")
}

function returnAction() {
    if (record === null) {
        returnToRecords();
    } else {
        returnToRecord();
    }
}

function confirmUpdate() {
    dataStore.updateRecord(record.id, createNewRecord())
}

function confirmCreate() {
    dataStore.createRecord(createNewRecord())
}

function confirmAction() {
    if (record === null) {
        confirmCreate();
    } else {
        confirmUpdate();
    }
    returnAction();
}

</script>

<template>
    <input class="happy-monkey-regular nameInput" v-model="nameValue">
    <div class="inputLine">
        <p class="ubuntu-regular">Date:</p>
        <select class="ubuntu-regular" v-model="monthNameValue">
            <option v-for="monthName of monthNames" :key="monthName">{{ monthName }}</option>
        </select>
        <input class="ubuntu-regular" type="number" v-model="dayValue" min="1" :max="daysInMonth">
        <input class="ubuntu-regular" type="number" v-model="yearValue">
    </div>
    <div class="inputLine">
        <p class="ubuntu-regular">Amount: $</p>
        <input class="ubuntu-regular" v-model="amountValue">
    </div>
    <div id="bankRecordButtons">
        <button class="ubuntu-regular" id="confirmButton" @click="confirmAction">Confirm</button>
        <button class="ubuntu-regular" id="cancelButton" @click="returnAction">Cancel</button>
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
#confirmButton {
    color: #050;
}
.inputLine {
    display: flex;
    flex-direction: row;
}
.nameInput {
    font-size: 18pt;
}
</style>