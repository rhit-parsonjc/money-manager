<script setup>
import { watch } from 'vue';
import { useRouter } from 'vue-router';

import DateAmountForm from '@/components/DateAmountForm.vue';
import DataMessages from '@/components/DataMessages.vue';
import useDataStore, { DataStatus } from '@/store/DataStore';

const dataStore = useDataStore();
const router = useRouter();

const { accountId, yearValue, monthValue, dayValue } = defineProps(["accountId", "yearValue", "monthValue", "dayValue"]);

watch(() => dataStore.retrievalStatus,
    (newDataStatus) => {
        if (newDataStatus === DataStatus.NOT_LOADED) {
            dataStore.loadDateAmountsDuringDayAsync(accountId, yearValue, monthValue, dayValue).catch(err => {
                if (err === 'Unauthorized') {
                    router.push('/');
                }
            })
        }
    },
    {immediate: true},
);
</script>

<template>
    <DataMessages :retrievalStatus="dataStore.dataStatus"
        loadingMessage="Loading Amount..." errorMessage="Could Not Load Amount">
        <DateAmountForm
            :accountId="accountId"
            :yearValue="yearValue"
            :monthValue="monthValue"
            :dayValue="dayValue"
            :amountValue="dataStore.data.dateAmounts[0].amount"
        />
    </DataMessages>
</template>