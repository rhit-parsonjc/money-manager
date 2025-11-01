<script setup>
/*
* RecordTransaction represents either a BankRecord or a FinancialTransaction
*/
import { useRouter } from 'vue-router';

import useDataStore from '@/store/DataStore';
import { formatCurrency } from '@/utilities/utilities';

const router = useRouter();
const dataStore = useDataStore();

const {accountId, data, isBankRecord} = defineProps(["accountId", "data", "isBankRecord"]);

function goToDetailPage() {
    dataStore.expireData();
    const urlPage = `/accounts/${accountId}` + (isBankRecord ? `/records/${data.id}` : `/transactions/${data.id}`);
    router.push(urlPage).then(dataStore.resetData);
}
</script>

<template>
    <li class="RecordTransaction m-0 mt-2">
        <a @click="goToDetailPage" class="ubuntu-regular RecordTransaction-text">
            {{ data.name }} ({{ formatCurrency(data.amount) }})
        </a>
    </li>
</template>