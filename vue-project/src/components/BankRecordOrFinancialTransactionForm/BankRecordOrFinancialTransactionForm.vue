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
    <input class="happy-monkey-regular BankRecordOrFinancialTransactionForm-name-input" v-model="nameValue">
    <div class="BankRecordOrFinancialTransactionForm-input-line">
        <p class="ubuntu-regular">Date:</p>
        <select class="ubuntu-regular" v-model="monthNameValue">
            <option v-for="monthName of monthNames" :key="monthName" class="ubuntu-regular">{{ monthName }}</option>
        </select>
        <input class="ubuntu-regular BankRecordOrFinancialTransactionForm-day-input" type="number" v-model="dayValue" min="1" :max="daysInMonth">
        <input class="ubuntu-regular BankRecordOrFinancialTransactionForm-year-input" type="number" v-model="yearValue">
    </div>
    <div class="BankRecordOrFinancialTransactionForm-input-line">
        <p class="ubuntu-regular">Amount: $</p>
        <input id="BankRecordOrFinancialTransactionForm-amount-input" class="ubuntu-regular" v-model="amountValue">
    </div>
    <div class="BankRecordOrFinancialTransactionForm-buttons">
        <button class="ubuntu-regular" id="BankRecordForm-confirm-button" @click="confirmAction">Confirm</button>
        <button class="ubuntu-regular" @click="returnAction">Cancel</button>
    </div>
</template>

<style scoped>
.BankRecordOrFinancialTransactionForm-name-input {
    font-size: 18pt;
    width: 14em;
}
.BankRecordOrFinancialTransactionForm-input-line {
    display: flex;
    flex-direction: row;
}
.BankRecordOrFinancialTransactionForm-day-input {
    width: 3em;
}
.BankRecordOrFinancialTransactionForm-year-input {
    width: 5em;
}
.BankRecordOrFinancialTransactionForm-amount-input {
    width: 10em;
}
.BankRecordOrFinancialTransactionForm-buttons button {
    background-color: white;
    font-size: 12pt;
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
}
.BankRecordOrFinancialTransactionForm-confirm-button {
    color: #050;
}
</style>