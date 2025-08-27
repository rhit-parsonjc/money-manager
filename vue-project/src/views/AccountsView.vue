<script setup>
import { watch } from 'vue';

import useDataStore, { DataStatus } from '@/store/DataStore';
import AccountItem from '@/components/AccountItem.vue';
import DataMessages from '@/components/DataMessages.vue';

const dataStore = useDataStore();

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
    dataStore.loadAccountsAsync();
}
</script>

<template>
    <h1 class="libre-baskerville-regular">Accounts</h1>
    <DataMessages :retrievalStatus="dataStore.dataStatus"
        loadingMessage="Loading Accounts..." errorMessage="Could Not Load Accounts">
        <ul>
            <AccountItem v-for="account of dataStore.data.accounts" :account="account" :key="account.id"/>
        </ul>
    </DataMessages>
</template>