<script setup>
import { watch } from 'vue';
import { useRouter } from 'vue-router';

import useDataStore, { DataStatus } from '@/store/DataStore';
import useAuthenticationStore from '@/store/AuthenticationStore';
import AccountItem from '@/components/AccountItem.vue';
import DataMessages from '@/components/DataMessages.vue';

const authenticationStore = useAuthenticationStore();
const dataStore = useDataStore();
const router = useRouter();

let username = authenticationStore.username;

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

function signOut() {
    authenticationStore.signOut();
    router.push("/");
}
</script>

<template>
    <h1 class="libre-baskerville-regular">Hello, {{username}}</h1>
    <h1 class="libre-baskerville-regular">Accounts</h1>
    <DataMessages :retrievalStatus="dataStore.dataStatus"
        loadingMessage="Loading Accounts..." errorMessage="Could Not Load Accounts">
        <ul>
            <AccountItem v-for="account of dataStore.data.accounts" :account="account" :key="account.id"/>
        </ul>
    </DataMessages>
    <h1 @click="signOut">Sign Out</h1>
</template>