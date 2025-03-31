<script setup>
/*
* BankRecordOrFinancialTransactionForm represents either a BankRecordForm
* or a FinancialTransactionForm
* Props
* - data (BankRecordModel if isBankRecord, else FinancialTransactionModel)
* - isBankRecord (true if BankRecordForm, false if FinancialTransactionForm)
*/
import { useRouter } from 'vue-router';
import { computed, ref } from 'vue';

import useDataStore from '@/store/DataStore';
import { monthNames, daysPerMonth, monthNameFromNumber } from '@/model/DateObjectModel';

const dataStore = useDataStore();
const router = useRouter();

const {data, isBankRecord} = defineProps(["data", "isBankRecord"]);

const initialFormValues = (data === null) ? {
    monthValue: 1,
    dayValue: 1,
    yearValue: 0,
    amountValue: 0,
    nameValue: "",
} : {
    monthValue: data.dateObj.monthValue,
    dayValue: data.dateObj.dayValue,
    yearValue: data.dateObj.yearValue,
    amountValue: data.amount.toFixed(2),
    nameValue: data.name,
};

const monthNameValue = ref(monthNameFromNumber(initialFormValues.monthValue));
const dayValue = ref(initialFormValues.dayValue);
const yearValue = ref(initialFormValues.yearValue);
const amountValue = ref(initialFormValues.amountValue);
const nameValue = ref(initialFormValues.nameValue);

const daysInMonth = computed(() => daysPerMonth(monthNameValue.value, yearValue.value));

function toItem() {
    return {
        year: yearValue.value,
        month: monthNames.indexOf(monthNameValue.value) + 1,
        day: dayValue.value,
        amount: amountValue.value,
        name: nameValue.value,
    };
}

function goToListAsync() {
    return router.push(isBankRecord ? "/records/" : "/transactions/");
}

function goToDetailsAsync(id) {
    return () => router.push(isBankRecord ? `/records/${id}` : `/transactions/${id}`);
}

function returnAction() {
    dataStore.expireData();
    if (data === null) {
        goToListAsync().then(dataStore.resetData);
    } else {
        goToDetailsAsync(data.id)().then(dataStore.resetData);
    }
}

function confirmAction() {
    if (data === null) {
        if (isBankRecord)
            dataStore.createBankRecordAsync(toItem())
                .then(goToListAsync)
                .then(dataStore.resetData);
        else
            dataStore.createFinancialTransactionAsync(toItem())
                .then(goToListAsync)
                .then(dataStore.resetData);
    } else {
        if (isBankRecord)
            dataStore.updateBankRecordAsync(data.id, toItem())
                .then(goToDetailsAsync(data.id))
                .then(dataStore.resetData);
        else
            dataStore.updateFinancialTransactionAsync(data.id, toItem())
                .then(goToDetailsAsync(data.id))
                .then(dataStore.resetData);
    }
}

</script>

<template>
    <h1 class="libre-baskerville-regular BankRecordOrFinancialTransactionForm-header">{{ (data === null ? "Create" : "Edit") + (isBankRecord ? " Bank Record" : " Financial Transaction") }}</h1>
    <div class="BankRecordOrFinancialTransactionForm-input-line">
        <p class="ubuntu-regular">Title:</p>
        <input class="happy-monkey-regular BankRecordOrFinancialTransactionForm-name-input" v-model="nameValue">
    </div>
    <div class="BankRecordOrFinancialTransactionForm-input-line">
        <p class="ubuntu-regular">Date:</p>
        <select class="happy-monkey-regular BankRecordOrFinancialTransactionForm-month-input" v-model="monthNameValue">
            <option v-for="monthName of monthNames" :key="monthName" class="happy-monkey-regular">{{ monthName }}</option>
        </select>
        <input class="happy-monkey-regular BankRecordOrFinancialTransactionForm-day-input" type="number" v-model="dayValue" min="1" :max="daysInMonth">
        <input class="happy-monkey-regular BankRecordOrFinancialTransactionForm-year-input" type="number" v-model="yearValue">
    </div>
    <div class="BankRecordOrFinancialTransactionForm-input-line">
        <p class="ubuntu-regular">Amount ($):</p>
        <input class="happy-monkey-regular BankRecordOrFinancialTransactionForm-amount-input" v-model="amountValue">
    </div>
    <div class="BankRecordOrFinancialTransactionForm-buttons">
        <button class="ubuntu-regular BankRecordOrFinancialTransactionForm-confirm-button" @click="confirmAction">Confirm</button>
        <button class="ubuntu-regular" @click="returnAction">Cancel</button>
    </div>
</template>

<style scoped>
.BankRecordOrFinancialTransactionForm-header {
  text-align: center;
  text-decoration: underline;
  margin-bottom: 1rem;
}
.BankRecordOrFinancialTransactionForm-input-line {
    display: flex;
    flex-direction: row;
    align-items: center;
    margin-bottom: 0.5rem;
}
.BankRecordOrFinancialTransactionForm-name-input {
    width: 14em;
    margin-left: 1rem;
}
.BankRecordOrFinancialTransactionForm-month-input {
    margin-left: 1rem;
}
.BankRecordOrFinancialTransactionForm-day-input {
    width: 3em;
    margin-left: 0.5rem;
}
.BankRecordOrFinancialTransactionForm-year-input {
    width: 5em;
    margin-left: 0.5rem;
}
.BankRecordOrFinancialTransactionForm-amount-input {
    width: 10em;
    margin-left: 1rem;
}
.BankRecordOrFinancialTransactionForm-buttons button {
    background-color: white;
    border-width: 0px;
}
.BankRecordOrFinancialTransactionForm-buttons button:hover {
    text-decoration: underline;
    cursor: pointer;
}
.BankRecordOrFinancialTransactionForm-buttons {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    margin-top: 1rem;
}
.BankRecordOrFinancialTransactionForm-confirm-button {
    color: #050;
}
</style>