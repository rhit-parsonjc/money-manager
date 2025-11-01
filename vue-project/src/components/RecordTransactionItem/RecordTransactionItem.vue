<script setup>
/*
* BankRecordOrFinancialTransactionItem represents either a
* BankRecordItem or a FinancialTransactionItem
*/
import { useRouter } from 'vue-router';

import useDataStore from '@/store/DataStore';
import { formatCurrency } from '@/utilities/utilities';

const dataStore = useDataStore();
const router = useRouter();

const { accountId, id, data, isBankRecord, isAttached } = defineProps(["accountId", "id", "data", "isBankRecord", "isAttached"]);

function gotoLink() {
    dataStore.expireData();
    const url = `/accounts/${accountId}` + (isBankRecord ? `/records/${data.id}` : `/transactions/${data.id}`);
    router.push(url).then(dataStore.resetData);
}

function attachOrDetach() {
    const recordId = isBankRecord ? data.id : id;
    const transactionId = isBankRecord ? id : data.id;
    if (isAttached)
        dataStore.detachRecordAndTransactionAsync(recordId, transactionId)
            .then(dataStore.resetData)
            .catch(err => {
                if (err === 'Unauthorized') {
                    router.push('/');
                }
            });
    else
        dataStore.attachRecordAndTransactionAsync(recordId, transactionId)
            .then(dataStore.resetData)
            .catch(err => {
                if (err === 'Unauthorized') {
                    router.push('/');
                }
            });
}

</script>

<template>
    <div :class="{
        'RecordTransactionItem-box': true,
        'card': true,
        'RecordTransactionItem-record': isBankRecord,
        'RecordTransactionItem-transaction': !isBankRecord,
        'p-3': true,
        'd-flex': true,
        'flex-column': true,
        'align-items-stretch': true,
        'flex-sm-row': true,
        'align-items-sm-center': true,
        'justify-content-sm-between': true,
    }">
        <div @click="gotoLink">
            <h2 class="ubuntu-regular card-title">{{ data.name }}</h2>
            <p class="ubuntu-regular card-text">{{ data.dateObj.format() }}</p>
            <p class="ubuntu-regular card-text">{{ formatCurrency(data.amount) }}</p>
        </div>
        <div @click="attachOrDetach">
            <button class="btn btn-primary btn-lg happy-monkey-regular">
                {{ isAttached ? "Detach" : "Attach"}}
            </button>
        </div>
    </div>
</template>

<style scoped>
.RecordTransactionItem-record {
    background-color: #0c0;
}
.RecordTransactionItem-transaction {
    background-color: #0cc;
}
.RecordTransactionItem-record:hover {
    background-color: #0f0;
}
.RecordTransactionItem-transaction:hover {
    background-color: #0ff;
}
.RecordTransactionItem-record button {
    background-color: #050;
}
.RecordTransactionItem-transaction button {
    background-color: #055;
}
.RecordTransactionItem-box:hover {
    cursor: pointer;
}
</style>