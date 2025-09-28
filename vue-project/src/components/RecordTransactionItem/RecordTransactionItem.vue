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
        'RecordTransactionItem-record': isBankRecord,
        'RecordTransactionItem-transaction': !isBankRecord,
    }">
        <div class="RecordTransactionItem-main-item" @click="gotoLink">
            <p class="ubuntu-regular">{{ data.name }}</p>
            <p class="ubuntu-regular">{{ data.dateObj.format() }}</p>
            <p class="ubuntu-regular">{{ formatCurrency(data.amount) }}</p>
        </div>
        <div class="RecordTransactionItem-icon" @click="attachOrDetach">
            <p :class="{
                'ubuntu-regular': true,
                'RecordTransactionItem-record-icon': isBankRecord,
                'RecordTransactionItem-transaction-icon': !isBankRecord,
            }">
                {{ isAttached ? "Detach" : "Attach"}}
            </p>
        </div>
    </div>
</template>

<style scoped>
.RecordTransactionItem-box {
    border: 1px solid black;
    padding: 0.5em;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
}
.RecordTransactionItem-main-item, .RecordTransactionItem-icon {
    cursor: pointer;
}
.RecordTransactionItem-record {
    background-color: #0c0;
}
.RecordTransactionItem-record:hover {
    background-color: #0f0;
}
.RecordTransactionItem-transaction {
    background-color: #0cc;
}
.RecordTransactionItem-transaction:hover {
    background-color: #0ff;
}
.RecordTransactionItem-record-icon {
    color: #050;
}
.RecordTransactionItem-transaction-icon {
    color: #055;
}
.RecordTransactionItem-record-icon:hover,
.RecordTransactionItem-transaction-icon:hover {
    text-decoration: underline;
}
</style>