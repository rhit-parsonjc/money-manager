<script setup>
import { useRouter } from 'vue-router';
import useDataStore from '@/store/DataStore';

const dataStore = useDataStore();
const router = useRouter();

function goToRecordsPage() {
    dataStore.expireData();
    router.push("/records").then(dataStore.resetData);
}

function goToTransactionsPage() {
    dataStore.expireData();
    router.push("/transactions").then(dataStore.resetData);
}

</script>

<template>
    <h1 class="libre-baskerville-regular" @click="goToRecordsPage">Records</h1>
    <h1 class="libre-baskerville-regular" @click="goToTransactionsPage">Transactions</h1>
</template>