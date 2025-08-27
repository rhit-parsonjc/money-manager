<script setup>
/*
* AttachRecordTransactions represents either an AttachBankRecords or an AttachFinancialTransactions
*/
import { computed } from 'vue';

import { RecordAndTransactionsModel } from '@/model/RecordAndTransactionsModel';
import { TransactionAndRecordsModel } from '@/model/TransactionAndRecordsModel';

import FinancialTransactionItem from '../RecordTransactionItem/FinancialTransactionItem.vue';
import BankRecordItem from '../RecordTransactionItem/BankRecordItem.vue';

const {item, subitems, isBankRecord} = defineProps(["accountId", "item", "subitems", "isBankRecord"]);

const dataModel = computed(() => {
    if (isBankRecord)
        return new RecordAndTransactionsModel(item, item.financialTransactions, subitems);
    else
        return new TransactionAndRecordsModel(item, item.bankRecords, subitems);
})

</script>

<template>
    <div v-if="isBankRecord">
        <h2 class="libre-baskerville-regular">Detach Financial Transactions</h2>
        <FinancialTransactionItem
            v-for="financialTransaction of dataModel.financialTransactions"
            class="AttachRecordTransactions-item"
            :key="financialTransaction.id"
            :recordId="item.id"
            :transaction="financialTransaction"
            :isAttached="true"
        />
        <h2 class="libre-baskerville-regular AttachRecordTransactions-attach-header">Attach Financial Transactions</h2>
        <FinancialTransactionItem
            v-for="financialTransaction of dataModel.otherFinancialTransactions"
            class="AttachRecordTransactions-item"
            :key="financialTransaction.id"
            :recordId="item.id"
            :transaction="financialTransaction"
            :isAttached="false"
        />
    </div>
    <div v-else>
        <h2 class="libre-baskerville-regular">Detach Bank Records</h2>
        <BankRecordItem
            v-for="bankRecord of dataModel.bankRecords"
            class="AttachRecordTransactions-item"
            :key="bankRecord.id"
            :transactionId="item.id"
            :record="bankRecord"
            :isAttached="true"
        />
        <h2 class="libre-baskerville-regular AttachRecordTransactions-attach-header">Attach Bank Records</h2>
        <BankRecordItem
            v-for="bankRecord of dataModel.otherBankRecords"
            class="AttachRecordTransactions-item"
            :key="bankRecord.id"
            :transactionId="item.id"
            :record="bankRecord"
            :isAttached="false"
        />
    </div>
</template>

<style scoped>
.AttachRecordTransactions-attach-header {
    margin-top: 2rem;
}
.AttachRecordTransactions-item {
    margin-top: 0.5rem;
}
</style>