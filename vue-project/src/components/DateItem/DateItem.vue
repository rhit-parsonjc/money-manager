<script setup>
/*
* DateItem represents either a DateItemAndRecords or a DateItemAndTransactions
*/
import { useRouter } from 'vue-router';

import BankRecord from '../RecordTransaction/BankRecord.vue';
import FinancialTransaction from '../RecordTransaction/FinancialTransaction.vue';
import { formatCurrency } from '@/utilities/utilities';

const router = useRouter();

const { accountId, data, displayBankRecords } = defineProps(["accountId", "data", "displayBankRecords"]);

const amount = displayBankRecords ? data.amount : null;

function goToDateAmountCreatePage() {
    const {yearValue, monthValue, dayValue} = data.dateObj;
    return router.push(`/accounts/${accountId}/amounts/${yearValue}/${monthValue}/${dayValue}/create`);
}

function goToDateAmountUpdatePage() {
    const {yearValue, monthValue, dayValue} = data.dateObj;
    return router.push(`/accounts/${accountId}/amounts/${yearValue}/${monthValue}/${dayValue}/update`)
}
</script>

<template>
    <li class="m0 mt-3">
        <div :class="{
            'DateItem-header': true,
            'DateItem-record-header': displayBankRecords,
            'DateItem-transaction-header': !displayBankRecords,
            'p-2': true,
            'd-inline-flex': true,
            'flex-row': true,
            'justify-content-between': true,
            'w-100': true,
        }">
            <h2 class="ubuntu-regular DateItem-header-text">
                {{data.dateObj.format()}}
                {{ amount !== null ? ("(" + formatCurrency(amount) + ")") : ""}}
            </h2>
            <button class="btn btn-primary btn-large w-25 happy-monkey-regular" @click="goToDateAmountCreatePage" v-if="displayBankRecords && amount === null">
                Add Amount
            </button>
            <button class="btn btn-primary btn-large w-25 happy-monkey-regular" @click="goToDateAmountUpdatePage" v-if="displayBankRecords && amount !== null">
                Edit Amount
            </button>
        </div>
        <ul class="DateItem-items p-0 ps-4 ps-sm-5" v-if="displayBankRecords && data.bankRecords.length > 0">
            <BankRecord
                v-for="record of data.bankRecords"
                :key="record.id"
                :accountId="accountId"
                :record="record"
            />
        </ul>
        <ul class="DateItem-items p-0 ps-4 ps-sm-5" v-else-if="!displayBankRecords && data.financialTransactions.length > 0">
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
.DateItem-record-header {
    background-color: #0c0;
}
.DateItem-transaction-header {
    background-color: #0cc;
}
.DateItem-items {
    list-style-type: none;
}
</style>