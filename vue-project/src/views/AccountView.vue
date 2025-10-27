<script setup>
import { watch } from 'vue';
import { useRouter } from 'vue-router';

import useDataStore, { DataStatus } from '@/store/DataStore';
import DataMessages from '@/components/DataMessages.vue';

const dataStore = useDataStore();
const router = useRouter();

const { accountId } = defineProps(["accountId"]);

watch(() => dataStore.dataStatus,
    (newDataStatus) => {
        if (newDataStatus === DataStatus.NOT_LOADED) {
            dataStore.loadSingleAccountAsync(accountId).catch(err => {
                if (err === 'Unauthorized') {
                    router.push('/');
                }
            })
        }
    },
    {immediate: true}
);

function goToRecordsPage() {
    dataStore.expireData();
    router.push(`/accounts/${accountId}/records`).then(dataStore.resetData);
}

function goToTransactionsPage() {
    dataStore.expireData();
    router.push(`/accounts/${accountId}/transactions`).then(dataStore.resetData);
}

function goToAccountsPage() {
    dataStore.expireData();
    router.push("/accounts").then(dataStore.resetData);
}

function goToEditAccountPage() {
    dataStore.expireData();
    router.push(`/accounts/${dataStore.data.account.id}/update`).then(dataStore.resetData);
}

</script>

<template>
    <DataMessages :retrievalStatus="dataStore.dataStatus"
    loadingMessage="Loading Account..." errorMessage="Could Not Load Accounts">
        <h1 class="libre-baskerville-regular">{{ dataStore.data.account.name }}</h1>
        <div id="AccountView-buttons">
            <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToRecordsPage">View Records</a>
            <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToTransactionsPage">View Transactions</a>
            <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToAccountsPage">View Accounts</a>
            <button class="btn btn-primary btn-lg happy-monkey-regular" @click="goToEditAccountPage">Edit Account</button>
        </div>
    </DataMessages>
</template>

<style scoped>
#AccountView-buttons {
    display: flex;
    flex-direction: column;
    align-items: center;
}
#AccountView-buttons * {
    margin-bottom: 1em;
}
#AccountView-buttons *:last-child {
    margin-bottom: 0em;
}
</style>