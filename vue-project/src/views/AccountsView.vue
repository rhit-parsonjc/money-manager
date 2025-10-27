<script setup>
import { watch } from 'vue';
import { useRouter } from 'vue-router';

import useDataStore, { DataStatus } from '@/store/DataStore';
import AccountItem from '@/components/AccountItem.vue';
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
</script>

<style scoped>
#AccountsView-buttons {
    display: flex;
    flex-direction: column;
    align-items: center;
}
#AccountsView-buttons * {
    margin-bottom: 1em;
}
#AccountsView-accounts-list {
    list-style-type: none;
    padding: 0;
    margin: 0;
}
</style>

<template>
    <h1 class="libre-baskerville-regular">Accounts</h1>
    <DataMessages :retrievalStatus="dataStore.dataStatus"
        loadingMessage="Loading Accounts..." errorMessage="Could Not Load Accounts">
        <div id="AccountsView-buttons">
            <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToProfilePage">View Profile</a>
            <button class="btn btn-primary btn-lg happy-monkey-regular" @click="goToCreateAccountPage">Create Account</button>
        </div>
        <ul id="AccountsView-accounts-list">
            <AccountItem v-for="account of dataStore.data.accounts" :account="account" :key="account.id"/>
        </ul>
    </DataMessages>
</template>