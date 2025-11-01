<script setup>
import { watch } from 'vue';
import { useRouter } from 'vue-router';

import useDataStore, { DataStatus } from '@/store/DataStore';
import DataMessages from '@/components/DataMessages.vue';

const dataStore = useDataStore();
const router = useRouter();

watch(() => dataStore.dataStatus,
    (newDataStatus) => {
        if (newDataStatus === DataStatus.NOT_LOADED) {
            loadData();
        }
    },
    {immediate: true}
);

function loadData() {
    console.log("Loading data...");
    dataStore.loadAccountsAsync().catch(err => {
        console.error(err);
        if (err === 'Unauthorized') {
            router.push('/');
        }
    });
}

function goToProfilePage() {
    return router.push("/profile");
}

function goToCreateAccountPage() {
    return router.push("/accounts/create");
}

function goToAccountPage(accountId) {
    dataStore.expireData();
    router.push(`/accounts/${accountId}`).then(dataStore.resetData);
}
</script>

<template>
    <div class="container-fluid p-3">
        <div class="row m-0 mb-3">
            <h1 class="libre-baskerville-regular text-center p-0">Accounts</h1>
        </div>
        <DataMessages :retrievalStatus="dataStore.dataStatus"
            loadingMessage="Loading Accounts..." errorMessage="Could Not Load Accounts">
            <div class="row justify-content-around m-0 mb-3">
                <div class="col-sm-5 col-md-4 p-0 mb-3 mb-sm-0">
                    <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToProfilePage">View Profile</a>
                </div>
                <div class="col-sm-5 col-md-4 p-0">
                    <button class="btn btn-primary btn-lg happy-monkey-regular" @click="goToCreateAccountPage">Create Account</button>
                </div>
            </div>
            <div class="row m-0">
                <div class="list-group">
                    <a class="ubuntu-regular list-group-item list-group-item-action" v-for="account of dataStore.data.accounts" :key="account.id" @click="goToAccountPage(account.id)">
                        {{ account.name }}
                    </a>
                </div>
            </div>
        </DataMessages>
    </div>
</template>