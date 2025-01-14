<script setup>
/**
 * BankRecordForm provides a form for users to create or update
 * An individual BankRecord
 * Props:
 * - record (type BankRecordModel)
 */
import useDataStore from '@/store/DataStore'
import { monthNames, daysPerMonth, monthNameFromNumber } from '@/model/DateObjectModel'
import { useRouter } from 'vue-router'
import { computed, ref } from 'vue'

const {record} = defineProps(["record"])

const dataStore = useDataStore()
const router = useRouter()

const initialFormValues = (record === null) ? {
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

let monthNameValue = ref(monthNameFromNumber(initialFormValues.monthValue))
let dayValue = ref(initialFormValues.dayValue)
let yearValue = ref(initialFormValues.yearValue)
let amountValue = ref(initialFormValues.amountValue)
let nameValue = ref(initialFormValues.nameValue)

const daysInMonth = computed(() => daysPerMonth(monthNameValue.value, yearValue.value))

function createNewRecord() {
    return {
        year: yearValue.value,
        month: monthNames.indexOf(monthNameValue.value) + 1,
        day: dayValue.value,
        amount: amountValue.value,
        name: nameValue.value,
    }
}

function goToRecordsAsync() {
    return router.push("/records/")
}

function goToRecordAsync(id) {
    return () => router.push(`/records/${id}`)
}

function returnAction() {
    dataStore.expireData()
    if (record === null) {
        goToRecordsAsync().then(dataStore.resetData)
    } else {
        goToRecordAsync(record.id)().then(dataStore.resetData)
    }
}

function confirmAction() {
    if (record === null) {
        dataStore.createRecordAsync(createNewRecord())
            .then(goToRecordsAsync)
            .then(dataStore.resetData)
    } else {
        dataStore.updateRecordAsync(record.id, createNewRecord())
            .then(goToRecordAsync(record.id))
            .then(dataStore.resetData)
    }
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