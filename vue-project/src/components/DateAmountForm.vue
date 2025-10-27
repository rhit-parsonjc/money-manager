<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

import useDataStore from '@/store/DataStore';
import { getAmountValue, getDollarsValue, getCentsValue } from '@/utilities/utilities';
import { DateObjectModel } from '@/model/DateObjectModel';

const dataStore = useDataStore();
const router = useRouter();

const { accountId, yearValue, monthValue, dayValue, amountValue } = defineProps(["accountId", "yearValue", "monthValue", "dayValue", "amountValue"]);

const dateObj = new DateObjectModel(yearValue, monthValue, dayValue);

const initialFormValues = (amountValue === null) ? {
    dollarsValue: 0,
    centsValue: 0,
} : {
    dollarsValue: getDollarsValue(amountValue),
    centsValue: getCentsValue(amountValue),
}

const dollarsValue = ref(initialFormValues.dollarsValue);
const centsValue = ref(initialFormValues.centsValue);

function goToRecordsPage() {
    router.push(`/accounts/${accountId}/records`);
}

function confirmAction() {
    const newAmountValue = getAmountValue(dollarsValue.value, centsValue.value);
    if (amountValue === null) {
        dataStore.createDateAmountAsync(accountId, {
            yearValue,
            monthValue,
            dayValue,
            amount: newAmountValue
        })
            .then(goToRecordsPage)
            .then(dataStore.resetData)
            .catch(err => {
                if (err === 'Unauthorized') {
                    router.push("/");
                }
            })
    } else {
        dataStore.updateDateAmountAsync(accountId, yearValue, monthValue, dayValue, newAmountValue)
            .then(goToRecordsPage)
            .then(dataStore.resetData)
            .catch(err => {
                if (err === 'Unauthorized') {
                    router.push("/");
                }
            })
    }
}
</script>

<template>
    <h1 class="libre-baskerville-regular">{{ amountValue === null ? "Create" : "Edit" }} Amount</h1>
    <h2 class="libre-baskerville-regular">{{ dateObj.format() }}</h2>
    <div id="DateAmountForm-form">
        <p class="ubuntu-regular">Amount ($)</p>
        <div class="DateAmountForm-input-line">
            <input class="ubuntu-regular" type="number" id="DateAmountForm-dollar-input" v-model="dollarsValue"/>
            <p class="ubuntu-regular">.</p>
            <input class="ubuntu-regular" type="number" id="DateAmountForm-cents-input" v-model="centsValue" min="0" max="99"/>
        </div>
        <div id="DateAmountForm-buttons">
            <input class="btn btn-lg btn-primary happy-monkey-regular" type="submit" value="Confirm" @click="confirmAction" />
            <a class="btn btn-lg btn-link happy-monkey-regular" @click="goToRecordsPage">Cancel</a>
        </div>
    </div>
</template>

<style scoped>
.DateAmountForm-input-line {
    display: flex;
    flex-direction: row;
    align-items: stretch;
    margin-bottom: 0.5em;
}
#DateAmountForm-dollar-input {
    width: 75%;
}
#DateAmountForm-cents-input {
    width: 25%;
}
</style>