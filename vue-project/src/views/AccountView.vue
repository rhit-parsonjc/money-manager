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
    <div class="container-fluid p-3">
        <DataMessages :retrievalStatus="dataStore.dataStatus"
        loadingMessage="Loading Account..." errorMessage="Could Not Load Accounts">
        <div class="row m-0 mb-3">
            <h1 class="libre-baskerville-regular text-center p-0">{{ dataStore.data.account.name }}</h1>
        </div>
        <div class="row m-0 justify-content-center">
            <div class="col-sm-5 col-md-4 p-0 mb-3 me-sm-5">
                <button class="btn btn-primary btn-lg happy-monkey-regular" @click="goToEditAccountPage">Edit Account</button>
            </div>
            <div class="col-sm-5 col-md-4 p-0 mb-3">
                <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToAccountsPage">View Accounts</a>
            </div>
        </div>
        <div class="row m-0 justify-content-center">
            <div class="col-sm-5 col-md-4 p-0 mb-3 me-sm-5">
                <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToRecordsPage">View Records</a>
            </div>
            <div class="col-sm-5 col-md-4 p-0">
                <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToTransactionsPage">View Transactions</a>
            </div>
        </div>
        </DataMessages>
    </div>
</template>