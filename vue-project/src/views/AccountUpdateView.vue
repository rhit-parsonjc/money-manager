<script setup>
import { watch } from 'vue';
import { useRouter } from 'vue-router';

import AccountForm from '@/components/AccountForm.vue';
import DataMessages from '@/components/DataMessages.vue';
import useDataStore, { DataStatus } from '@/store/DataStore';

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

</script>

<template>
    <div class="container-fluid p-3">
        <DataMessages :retrievalStatus="dataStore.dataStatus"
            loadingMessage="Loading Account..." errorMessage="Could Not Load Account">
            <AccountForm :account="dataStore.data.account" />
        </DataMessages>
    </div>
</template>