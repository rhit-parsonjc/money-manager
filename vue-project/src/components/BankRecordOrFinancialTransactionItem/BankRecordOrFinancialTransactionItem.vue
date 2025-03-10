<script setup>
/*
* BankRecordOrFinancialTransactionItem represents either a
* BankRecordItem or a FinancialTransactionItem
* Props
* - id (record ID if isBankRecord, transaction ID otherwise)
* - data (BankRecordModel if isBankRecord, else FinancialTransactionModel)
* - isBankRecord (true if BankRecordItem, false if FinancialTransactionItem)
* - isAttached (true if already attached, false if not attached)
*/
import { useRouter } from 'vue-router';

import useDataStore from '@/store/DataStore';
import { formatCurrency } from '@/utilities/utilities';

const dataStore = useDataStore();
const router = useRouter();

const {id, data, isBankRecord, isAttached} = defineProps(["id", "data", "isBankRecord", "isAttached"]);

function gotoLink() {
    dataStore.expireData();
    const url = isBankRecord ? `/records/${data.id}` : `/transactions/${data.id}`;
    router.push(url).then(dataStore.resetData);
}

function attachOrDetach() {
    const recordId = isBankRecord ? data.id : id;
    const transactionId = isBankRecord ? id : data.id;
    if (isAttached)
        dataStore.detachRecordAndTransaction(recordId, transactionId)
        .then(dataStore.resetData);
    else
        dataStore.attachRecordAndTransaction(recordId, transactionId)
        .then(dataStore.resetData)
}

</script>

<template>
    <div :class="{
        'BankRecordOrFinancialTransactionItem-box': true,
        'BankRecordOrFinancialTransactionItem-record': isBankRecord,
        'BankRecordOrFinancialTransactionItem-transaction': !isBankRecord,
     }">
        <div class="BankRecordOrFinancialTransactionItem-main-item" @click="gotoLink">
            <p class="ubuntu-regular">{{ data.name }}</p>
            <p class="ubuntu-regular">{{ data.dateObj.format() }}</p>
            <p class="ubuntu-regular">{{ formatCurrency(data.amount) }}</p>
        </div>
        <div class="BankRecordOrFinancialTransactionItem-icon" @click="attachOrDetach">
            <p :class="{
                'ubuntu-regular': true,
                'BankRecordOrFinancialTransactionItem-record-icon': isBankRecord,
                'BankRecordOrFinancialTransactionItem-transaction-icon': !isBankRecord,
            }">{{ isAttached ? "Detach" : "Attach"}}</p>
        </div>
    </div>
</template>

<style scoped>
.BankRecordOrFinancialTransactionItem-box {
    border: 1px solid black;
    margin: 1em;
    padding: 0.2em;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
}
.BankRecordOrFinancialTransactionItem-main-item, .BankRecordOrFinancialTransactionItem-icon {
    cursor: pointer;
}
.BankRecordOrFinancialTransactionItem-record {
    background-color: #0c0;
}
.BankRecordOrFinancialTransactionItem-transaction {
    background-color: #0cc;
}
.BankRecordOrFinancialTransactionItem-record-icon {
    color: #050;
}
.BankRecordOrFinancialTransactionItem-transaction-icon {
    color: #055;
}
</style>