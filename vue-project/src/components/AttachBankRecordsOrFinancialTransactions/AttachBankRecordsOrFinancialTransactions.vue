<script setup>
/*
* AttachBankRecordsOrFinancialTransactions represents either an
* AttachBankRecords or AttachFinancialTransactions
* Props
* - item (BankRecordModel if isBankRecord, else FinancialTransactionModel)
* - subitems (list of FinancialTransactionModel objects if isBankRecord,
              else list of BankRecordModel objects)
* - isBankRecord (true if AttachBankRecords, false if AttachFinancialTransactions)
*/
import { computed } from 'vue';

import { RecordAndTransactionsModel } from '@/model/RecordAndTransactionsModel';
import { TransactionAndRecordsModel } from '@/model/TransactionAndRecordsModel';

import FinancialTransactionItem from '../BankRecordOrFinancialTransactionItem/FinancialTransactionItem.vue';
import BankRecordItem from '../BankRecordOrFinancialTransactionItem/BankRecordItem.vue';

const {item, subitems, isBankRecord} = defineProps(["item", "subitems", "isBankRecord"])

const dataModel = computed(() => {
    if (isBankRecord)
        return new RecordAndTransactionsModel(item, item.financialTransactions, subitems)
    else
        return new TransactionAndRecordsModel(item, item.bankRecords, subitems)
})

</script>
<template>
    <h1 class="happy-monkey-regular">{{ item.name }}</h1>
    <div v-if="isBankRecord">
        <p class="ubuntu-regular">Detach Transactions</p>
        <FinancialTransactionItem
            v-for="financialTransaction of dataModel.financialTransactions"
            :key="financialTransaction.id"
            :recordId="item.id"
            :transaction="financialTransaction"
            :isAttached="true"/>
        <p class="ubuntu-regular">Attach Transactions</p>
        <FinancialTransactionItem
            v-for="financialTransaction of dataModel.otherFinancialTransactions"
            :key="financialTransaction.id"
            :recordId="item.id"
            :transaction="financialTransaction"
            :isAttached="false"/>
    </div>
    <div v-else>
        <p class="ubuntu-regular">Detach Records</p>
        <BankRecordItem
            v-for="bankRecord of dataModel.bankRecords"
            :key="bankRecord.id"
            :transactionId="item.id"
            :record="bankRecord"
            :isAttached="true"/>
        <p class="ubuntu-regular">Attach Records</p>
        <BankRecordItem
            v-for="bankRecord of dataModel.otherBankRecords"
            :key="bankRecord.id"
            :transactionId="item.id"
            :record="bankRecord"
            :isAttached="false"/>
    </div>
</template>