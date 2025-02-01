<script setup>
/**
 * BankRecord displays information about a single bank record,
 * Used in a list view.
 * Props:
 * - record (type BankRecordModel)
 */
import useDataStore from '@/store/DataStore';
import { formatCurrency } from '@/utilities/utilities'
import { useRouter } from 'vue-router';

const {record} = defineProps(["record"])

const router = useRouter()
const dataStore = useDataStore()

const recordUrl = "/records/" + record.id;

function goToRecord() {
    dataStore.expireData()
    router.push(recordUrl).then(dataStore.resetData)
}
</script>

<template>
    <li class="BankRecord-record">
        <a @click="goToRecord" class="ubuntu-regular BankRecord-text">
            {{ record.name }} ({{ formatCurrency(record.amount) }})
        </a>
    </li>
</template>

<style scoped>
.BankRecord-record {
    margin: 0.5rem 0em;
}
.BankRecord-text {
    font-size: 12pt;
    color: black;
    text-decoration: none;
}
.BankRecord-text:hover {
    text-decoration: underline;
}
</style>