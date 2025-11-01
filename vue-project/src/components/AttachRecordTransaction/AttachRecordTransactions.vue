<script setup>
/*
* AttachRecordTransactions represents either an AttachBankRecords or an AttachFinancialTransactions
*/
import { computed } from 'vue';

import { RecordAndTransactionsModel } from '@/model/RecordAndTransactionsModel';
import { TransactionAndRecordsModel } from '@/model/TransactionAndRecordsModel';

import FinancialTransactionItem from '../RecordTransactionItem/FinancialTransactionItem.vue';
import BankRecordItem from '../RecordTransactionItem/BankRecordItem.vue';

const {accountId, item, subitems, isBankRecord} = defineProps(["accountId", "item", "subitems", "isBankRecord"]);

const dataModel = computed(() => {
    if (isBankRecord)
        return new RecordAndTransactionsModel(item, item.financialTransactions, subitems);
    else
        return new TransactionAndRecordsModel(item, item.bankRecords, subitems);
});

const attachedItemCount = computed(() => {
    if (isBankRecord)
        return item.financialTransactions.length;
    else
        return item.bankRecords.length;
});

const otherItemCount = computed(() => {
    if (isBankRecord)
        return subitems.length - item.financialTransactions.length;
    else
        return subitems.length - item.bankRecords.length;
});

</script>

<template>
    <div v-if="isBankRecord">
        <div class="row m-0">
            <h2 class="libre-baskerville-regular text-center p-0">Attached {{ attachedItemCount == 1 ? "Transaction" : "Transactions" }}</h2>
        </div>
        <FinancialTransactionItem
            v-for="financialTransaction of dataModel.financialTransactions"
            class="mt-2"
            :key="financialTransaction.id"
            :accountId="accountId"
            :recordId="item.id"
            :transaction="financialTransaction"
            :isAttached="true"
        />
        <div class="row m-0 mt-3">
            <h2 class="libre-baskerville-regular text-center p-0">Other {{ otherItemCount == 1 ? "Transaction" : "Transactions" }}</h2>
        </div>
        <FinancialTransactionItem
            v-for="financialTransaction of dataModel.otherFinancialTransactions"
            class="mt-2"
            :key="financialTransaction.id"
            :accountId="accountId"
            :recordId="item.id"
            :transaction="financialTransaction"
            :isAttached="false"
        />
    </div>
    <div v-else>
        <div class="row m-0">
            <h2 class="libre-baskerville-regular text-center p-0">Attached {{ attachedItemCount == 1 ? "Record" : "Records" }}</h2>
        </div>
        <BankRecordItem
            v-for="bankRecord of dataModel.bankRecords"
            class="mt-2"
            :key="bankRecord.id"
            :accountId="accountId"
            :transactionId="item.id"
            :record="bankRecord"
            :isAttached="true"
        />
        <div class="row m-0 mt-3">
            <h2 class="libre-baskerville-regular text-center p-0">Other {{ otherItemCount == 1 ? "Record" : "Records" }}</h2>
        </div>
        <BankRecordItem
            v-for="bankRecord of dataModel.otherBankRecords"
            class="mt-2"
            :key="bankRecord.id"
            :accountId="accountId"
            :transactionId="item.id"
            :record="bankRecord"
            :isAttached="false"
        />
    </div>
</template>