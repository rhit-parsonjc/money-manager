<script setup>
/*
* BankRecordOrFinancialTransactionForm represents either a BankRecordForm
* or a FinancialTransactionForm
*/
import { useRouter } from 'vue-router';
import { computed, ref } from 'vue';

import useDataStore from '@/store/DataStore';
import { monthNames, daysPerMonth, monthNameFromNumber } from '@/model/DateObjectModel';

const dataStore = useDataStore();
const router = useRouter();

const {accountId, data, isBankRecord} = defineProps(["accountId", "data", "isBankRecord"]);

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
    amountValue: data.amount,
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
        yearValue: yearValue.value,
        monthValue: monthNames.indexOf(monthNameValue.value) + 1,
        dayValue: dayValue.value,
        amount: amountValue.value,
        name: nameValue.value,
    };
}

function goToListAsync() {
    return router.push(`/accounts/${accountId}` + (isBankRecord ? "/records/" : "/transactions/"));
}

function goToDetailsAsync(id) {
    return () => router.push(`/accounts/${accountId}` + (isBankRecord ? `/records/${id}` : `/transactions/${id}`));
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
            dataStore.createBankRecordAsync(accountId, toItem())
                .then(goToListAsync)
                .then(dataStore.resetData)
                .catch(err => {
                    if (err === 'Unauthorized') {
                        router.push('/');
                    }
                });
        else
            dataStore.createFinancialTransactionAsync(accountId, toItem())
                .then(goToListAsync)
                .then(dataStore.resetData)
                .catch(err => {
                    if (err === 'Unauthorized') {
                        router.push('/');
                    }
                });
    } else {
        if (isBankRecord)
            dataStore.updateBankRecordAsync(accountId, data.id, toItem())
                .then(goToDetailsAsync(data.id))
                .then(dataStore.resetData)
                .catch(err => {
                    if (err === 'Unauthorized') {
                        router.push('/');
                    }
                });
        else
            dataStore.updateFinancialTransactionAsync(accountId, data.id, toItem())
                .then(goToDetailsAsync(data.id))
                .then(dataStore.resetData)
                .catch(err => {
                    if (err === 'Unauthorized') {
                        router.push('/');
                    }
                });
    }
}

</script>

<template>
    <h1 class="libre-baskerville-regular RecordTransactionForm-header">{{ (data === null ? "Create" : "Edit") + (isBankRecord ? " Bank Record" : " Financial Transaction") }}</h1>
    <div class="RecordTransactionForm-input-line">
        <p class="ubuntu-regular">Name:</p>
        <input class="happy-monkey-regular RecordTransactionForm-name-input" v-model="nameValue">
    </div>
    <div class="RecordTransactionForm-input-line">
        <p class="ubuntu-regular">Date:</p>
        <select class="happy-monkey-regular RecordTransactionForm-month-input" v-model="monthNameValue">
            <option v-for="monthName of monthNames" :key="monthName" class="happy-monkey-regular">{{ monthName }}</option>
        </select>
        <input class="happy-monkey-regular RecordTransactionForm-day-input" type="number" v-model="dayValue" min="1" :max="daysInMonth">
        <input class="happy-monkey-regular RecordTransactionForm-year-input" type="number" v-model="yearValue">
    </div>
    <div class="RecordTransactionForm-input-line">
        <p class="ubuntu-regular">Amount ($):</p>
        <input class="happy-monkey-regular RecordTransactionForm-amount-input" v-model="amountValue">
    </div>
    <div class="RecordTransactionForm-buttons">
        <button class="ubuntu-regular RecordTransactionForm-confirm-button" @click="confirmAction">Confirm</button>
        <button class="ubuntu-regular" @click="returnAction">Cancel</button>
    </div>
</template>

<style scoped>
.RecordTransactionForm-header {
  text-align: center;
  text-decoration: underline;
  margin-bottom: 1rem;
}
.RecordTransactionForm-input-line {
    display: flex;
    flex-direction: row;
    align-items: center;
    margin-bottom: 0.5rem;
}
.RecordTransactionForm-name-input {
    width: 14em;
    margin-left: 1rem;
}
.RecordTransactionForm-month-input {
    margin-left: 1rem;
}
.RecordTransactionForm-day-input {
    width: 3em;
    margin-left: 0.5rem;
}
.RecordTransactionForm-year-input {
    width: 5em;
    margin-left: 0.5rem;
}
.RecordTransactionForm-amount-input {
    width: 10em;
    margin-left: 1rem;
}
.RecordTransactionForm-buttons button {
    background-color: white;
    border-width: 0px;
}
.RecordTransactionForm-buttons button:hover {
    text-decoration: underline;
    cursor: pointer;
}
.RecordTransactionForm-buttons {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    margin-top: 1rem;
}
.RecordTransactionForm-confirm-button {
    color: #050;
}
</style>