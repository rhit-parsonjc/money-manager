<script setup>
/*
* DateItem represents either a DateItemAndRecords or a DateItemAndTransactions
*/
import { ref } from 'vue';
import useDataStore from '@/store/DataStore';

import BankRecord from '../RecordTransaction/BankRecord.vue';
import FinancialTransaction from '../RecordTransaction/FinancialTransaction.vue';
import { formatCurrency } from '@/utilities/utilities';

const dataStore = useDataStore();

const { accountId, data, displayBankRecords } = defineProps(["accountId", "data", "displayBankRecords"]);
const isEditing = ref(false);

const amount = displayBankRecords ? data.amount : null;
const hasAmount = amount !== null;
const amountValue = ref(amount);

function beginAddingOrEditing() {
    isEditing.value = true;
}

function confirmAddOrEdit() {
    const newAmount = {
        yearValue: data.dateObj.yearValue,
        monthValue: data.dateObj.monthValue,
        dayValue: data.dateObj.dayValue,
        amount: amountValue.value,
    };
    if (hasAmount) {
        dataStore
            .updateDateAmountAsync(newAmount.year, newAmount.month, newAmount.day, newAmount.amount)
            .then(dataStore.resetData);
    } else {
        dataStore
            .createDateAmountAsync(newAmount)
            .then(dataStore.resetData);
    }
}

function clearAmount() {
    const {yearValue, monthValue, dayValue} = data.dateObj;
    dataStore
        .deleteDateAmountAsync(yearValue, monthValue, dayValue)
        .then(dataStore.resetData);
}

function cancelAddOrEdit() {
    isEditing.value = false;
}
</script>

<template>
    <li>
        <div :class="{
            'DateItem-header': true,
            'DateItem-record-header': displayBankRecords,
            'DateItem-transaction-header': !displayBankRecords
        }">
            <h2 class="happy-monkey-regular DateItem-header-text">
                {{data.dateObj.format()}}
                {{ hasAmount ? ("(" + formatCurrency(amount) + ")") : ""}}
            </h2>
            <button class="ubuntu-regular" @click="beginAddingOrEditing" v-if="displayBankRecords && !isEditing">
                {{ hasAmount ? "Edit" : "Add" }}
            </button>
            <button class="ubuntu-regular" @click="clearAmount" v-if="hasAmount && displayBankRecords && !isEditing">
                Clear
            </button>
            <input class="happy-monkey-regular DateItem-amount-input" type="number" v-model="amountValue" v-if="displayBankRecords && isEditing">
            <button class="ubuntu-regular" @click="confirmAddOrEdit" v-if="displayBankRecords && isEditing">Confirm</button>
            <button class="ubuntu-regular" @click="cancelAddOrEdit" v-if="displayBankRecords && isEditing">Cancel</button>
        </div>
        <ul class="DateItem-items" v-if="displayBankRecords && data.bankRecords.length > 0">
            <BankRecord
                v-for="record of data.bankRecords"
                :key="record.id"
                :accountId="accountId"
                :record="record"
            />
        </ul>
        <ul class="DateItem-items" v-else-if="!displayBankRecords && data.financialTransactions.length > 0">
            <FinancialTransaction
                v-for="transaction of data.financialTransactions"
                :key="transaction.id"
                :accountId="accountId"
                :transaction="transaction"
            />
        </ul>
    </li>
</template>

<style scoped>
.DateItem-header {
    padding: 0.5rem;
    margin: 0rem 0rem 0.25rem 0rem;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
}
.DateItem-header * {
    margin-right: 0.5rem;
}
.DateItem-header *:first-child {
    margin-right: 1rem;
}
.DateItem-header *:last-child {
    margin-right: 0rem;
}
.DateItem-record-header, .DateItem-record-header * {
    background-color: #0c0;
}
.DateItem-record-header button:focus {
    background-color: #050;
}
.DateItem-transaction-header, .DateItem-transaction-header * {
    background-color: #0cc;
}
.DateItem-transaction-header button:focus {
    background-color: #055;
}
.DateItem-amount-input {
    width: 7em;
}
.DateItem-items {
    list-style-type: none;
    padding-left: 2rem;
    margin-bottom: 1rem;
}
</style>