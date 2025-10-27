<script setup>
/*
* BankRecordOrFinancialTransactionForm represents either a BankRecordForm
* or a FinancialTransactionForm
*/
import { useRouter } from 'vue-router';
import { computed, ref } from 'vue';

import useDataStore from '@/store/DataStore';
import { monthNames, daysPerMonth, monthNameFromNumber } from '@/model/DateObjectModel';
import { getAmountValue, getCentsValue, getDollarsValue } from '@/utilities/utilities';

const dataStore = useDataStore();
const router = useRouter();

const {accountId, data, isBankRecord} = defineProps(["accountId", "data", "isBankRecord"]);

const initialFormValues = (data === null) ? {
    nameValue: "",
    monthValue: 1,
    dayValue: 1,
    yearValue: 0,
    dollarsValue: 0,
    centsValue: 0,
} : {
    nameValue: data.name,
    monthValue: data.dateObj.monthValue,
    dayValue: data.dateObj.dayValue,
    yearValue: data.dateObj.yearValue,
    dollarsValue: getDollarsValue(data.amount),
    centsValue: getCentsValue(data.amount),
};

const monthNameValue = ref(monthNameFromNumber(initialFormValues.monthValue));
const dayValue = ref(initialFormValues.dayValue);
const yearValue = ref(initialFormValues.yearValue);
const nameValue = ref(initialFormValues.nameValue);
const dollarsValue = ref(initialFormValues.dollarsValue);
const centsValue = ref(initialFormValues.centsValue);

const daysInMonth = computed(() => daysPerMonth(monthNameValue.value, yearValue.value));

function toItem() {
    return {
        yearValue: yearValue.value,
        monthValue: monthNames.indexOf(monthNameValue.value) + 1,
        dayValue: dayValue.value,
        amount: getAmountValue(dollarsValue.value, centsValue.value),
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
    <h1 class="libre-baskerville-regular">{{ (data === null ? "Create" : "Edit") + (isBankRecord ? " Record" : " Transaction") }}</h1>
    <div id="RecordTransactionForm-form">
        <label for="RecordTransactionForm-name-input" class="ubuntu-regular">Name</label>
        <div class="RecordTransactionForm-input-line">
            <input class="ubuntu-regular" id="RecordTransactionForm-name-input" v-model="nameValue">
        </div>
        <p class="ubuntu-regular">Date</p>
        <div class="RecordTransactionForm-input-line">
            <select class="ubuntu-regular" id="RecordTransactionForm-month-input" v-model="monthNameValue">
                <option v-for="monthName of monthNames" :key="monthName" class="ubuntu-regular">{{ monthName }}</option>
            </select>
            <input class="ubuntu-regular" id="RecordTransactionForm-day-input" type="number" v-model="dayValue" min="1" :max="daysInMonth">
            <input class="ubuntu-regular" id="RecordTransactionForm-year-input" type="number" v-model="yearValue">
        </div>
        <p class="ubuntu-regular">Amount ($)</p>
        <div class="RecordTransactionForm-input-line">
            <input class="ubuntu-regular" type="number" id="RecordTransactionForm-dollar-input" v-model="dollarsValue"/>
            <p class="ubuntu-regular">.</p>
            <input class="ubuntu-regular" type="number" id="RecordTransactionForm-cents-input" v-model="centsValue" min="0" max="99"/>
        </div>
        <div id="RecordTransactionForm-buttons">
            <input class="btn btn-lg btn-primary happy-monkey-regular" type="submit" value="Confirm" @click="confirmAction" />
            <a class="btn btn-lg btn-link happy-monkey-regular" @click="returnAction">Cancel</a>
        </div>
    </div>
</template>

<style scoped>
.RecordTransactionForm-input-line {
    display: flex;
    flex-direction: row;
    align-items: stretch;
    margin-bottom: 0.5em;
}
#RecordTransactionForm-name-input {
    width: 100%;
}
#RecordTransactionForm-month-input {
    width: 60%;
}
#RecordTransactionForm-day-input {
    width: 15%;
}
#RecordTransactionForm-year-input {
    width: 25%;
}
#RecordTransactionForm-dollar-input {
    width: 75%;
}
#RecordTransactionForm-cents-input {
    width: 25%;
}
#RecordTransactionForm-buttons {
    display: flex;
    width: 100%;
    justify-content: space-between;
    margin-top: 1em;
}
</style>