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
        dataStore.createBankRecordAsync(createNewRecord())
            .then(goToRecordsAsync)
            .then(dataStore.resetData)
    } else {
        dataStore.updateBankRecordAsync(record.id, createNewRecord())
            .then(goToRecordAsync(record.id))
            .then(dataStore.resetData)
    }
}

</script>

<template>
    <input class="happy-monkey-regular BankRecordForm-name-input" v-model="nameValue">
    <div class="BankRecordForm-input-line">
        <p class="ubuntu-regular">Date:</p>
        <select class="ubuntu-regular" v-model="monthNameValue">
            <option v-for="monthName of monthNames" :key="monthName">{{ monthName }}</option>
        </select>
        <input class="ubuntu-regular BankRecordForm-day-input" type="number" v-model="dayValue" min="1" :max="daysInMonth">
        <input class="ubuntu-regular BankRecordForm-year-input" type="number" v-model="yearValue">
    </div>
    <div class="BankRecordForm-input-line">
        <p class="ubuntu-regular">Amount: $</p>
        <input id="BankRecordForm-amount-input" class="ubuntu-regular" v-model="amountValue">
    </div>
    <div class="BankRecordForm-buttons">
        <button class="ubuntu-regular" id="BankRecordForm-confirm-button" @click="confirmAction">Confirm</button>
        <button class="ubuntu-regular" @click="returnAction">Cancel</button>
    </div>
</template>

<style scoped>
.BankRecordForm-name-input {
    font-size: 18pt;
    width: 14em;
}
.BankRecordForm-input-line {
    display: flex;
    flex-direction: row;
}
.BankRecordForm-day-input {
    width: 3em;
}
.BankRecordForm-year-input {
    width: 5em;
}
.BankRecordForm-amount-input {
    width: 10em;
}
.BankRecordForm-buttons button {
    background-color: white;
    font-size: 12pt;
    border-width: 0px;
}
.BankRecordForm-buttons button:hover {
    text-decoration: underline;
    cursor: pointer;
}
.BankRecordForm-buttons {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
}
.BankRecordForm-confirm-button {
    color: #050;
}
</style>