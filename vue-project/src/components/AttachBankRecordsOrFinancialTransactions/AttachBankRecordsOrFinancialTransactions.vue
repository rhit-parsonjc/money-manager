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
    <div v-if="isBankRecord">
        <h2 class="libre-baskerville-regular">Detach Financial Transactions</h2>
        <FinancialTransactionItem
            v-for="financialTransaction of dataModel.financialTransactions"
            class="AttachBankRecordsOrFinancialTransactions-item"
            :key="financialTransaction.id"
            :recordId="item.id"
            :transaction="financialTransaction"
            :isAttached="true"/>
        <h2 class="libre-baskerville-regular AttachBankRecordsOrFinancialTransactions-attach-header">Attach Financial Transactions</h2>
        <FinancialTransactionItem
            v-for="financialTransaction of dataModel.otherFinancialTransactions"
            class="AttachBankRecordsOrFinancialTransactions-item"
            :key="financialTransaction.id"
            :recordId="item.id"
            :transaction="financialTransaction"
            :isAttached="false"/>
    </div>
    <div v-else>
        <h2 class="libre-baskerville-regular">Detach Bank Records</h2>
        <BankRecordItem
            v-for="bankRecord of dataModel.bankRecords"
            class="AttachBankRecordsOrFinancialTransactions-item"
            :key="bankRecord.id"
            :transactionId="item.id"
            :record="bankRecord"
            :isAttached="true"/>
        <h2 class="libre-baskerville-regular AttachBankRecordsOrFinancialTransactions-attach-header">Attach Bank Records</h2>
        <BankRecordItem
            v-for="bankRecord of dataModel.otherBankRecords"
            class="AttachBankRecordsOrFinancialTransactions-item"
            :key="bankRecord.id"
            :transactionId="item.id"
            :record="bankRecord"
            :isAttached="false"/>
    </div>
</template>

<style scoped>
.AttachBankRecordsOrFinancialTransactions-attach-header {
    margin-top: 2rem;
}
.AttachBankRecordsOrFinancialTransactions-item {
    margin-top: 0.5rem;
}
</style>