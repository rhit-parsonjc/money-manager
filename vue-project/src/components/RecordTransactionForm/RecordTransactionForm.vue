<script setup>
/*
* BankRecordOrFinancialTransactionForm represents either a BankRecordForm
* or a FinancialTransactionForm
*/
import { useRouter } from 'vue-router';
import { ref } from 'vue';

import useDataStore from '@/store/DataStore';
import { dateToStr, getAmountValue, getCentsValue, getDollarsValue, parseDate } from '@/utilities/utilities';

const dataStore = useDataStore();
const router = useRouter();

const {accountId, data, isBankRecord} = defineProps(["accountId", "data", "isBankRecord"]);

const initialFormValues = (data === null) ? {
    nameValue: "",
    dateValue: null,
    dollarsValue: 0,
    centsValue: 0,
} : {
    nameValue: data.name,
    dateValue: dateToStr(data.dateObj.yearValue, data.dateObj.monthValue, data.dateObj.dayValue),
    dollarsValue: getDollarsValue(data.amount),
    centsValue: getCentsValue(data.amount),
};

const nameValue = ref(initialFormValues.nameValue);
const dateValue = ref(initialFormValues.dateValue);
const dollarsValue = ref(initialFormValues.dollarsValue);
const centsValue = ref(initialFormValues.centsValue);

function toItem() {
    const dateParsed = parseDate(dateValue.value);
    return {
        yearValue: dateParsed.year,
        monthValue: dateParsed.month,
        dayValue: dateParsed.day,
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
    <div class="row m-0 mb-3">
        <h1 class="libre-baskerville-regular text-center p-0">{{ (data === null ? "Create" : "Edit") + (isBankRecord ? " Record" : " Transaction") }}</h1>
    </div>
    <div class="row m-0 mb-2 justify-content-center">
        <div class="col-sm-2 text-sm-end p-0 pe-sm-3">
            <label for="RecordTransactionForm-name-input" class="ubuntu-regular">Name</label>
        </div>
        <div class="col-sm-8 p-0">
            <input class="ubuntu-regular" id="RecordTransactionForm-name-input" v-model="nameValue">
        </div>
    </div>
    <div class="row m-0 mb-2 justify-content-center">
        <div class="col-sm-2 text-sm-end p-0 pe-sm-3">
            <label for="RecordTransactionForm-date-input" class="ubuntu-regular">Date</label>
        </div>
        <div class="col-sm-4 col-md-3 p-0">
            <input type="date" class="ubuntu-regular" id="RecordTransactionForm-date-input" v-model="dateValue"/>
        </div>
    </div>
    <div class="row m-0 mb-3 justify-content-center">
        <div class="col-12 col-sm-4 text-sm-end p-0 pe-sm-3">
            <p class="ubuntu-regular">Amount ($)</p>
        </div>
        <div class="col-7 col-sm-5 p-0">
            <input class="ubuntu-regular" type="number" v-model="dollarsValue"/>
        </div>
        <div class="col-1 p-0 d-inline-flex justify-content-center">
            <p class="ubuntu-regular">.</p>
        </div>
        <div class="col-2 col-sm-1 p-0">
            <input class="ubuntu-regular" type="number" v-model="centsValue" min="0" max="99"/>
        </div>
    </div>
    <div class="row m-0 justify-content-around">
        <div class="col-5 col-sm-3 col-md-2 p-0">
            <input class="btn btn-lg btn-primary happy-monkey-regular" type="submit" value="Confirm" @click="confirmAction" />
        </div>
        <div class="col-5 col-sm-3 col-md-2 p-0">
            <a class="btn btn-lg btn-link happy-monkey-regular" @click="returnAction">Cancel</a>
        </div>
    </div>
</template>