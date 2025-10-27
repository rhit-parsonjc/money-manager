<script setup>
/*
 * Account displays a single account to be used in a list view
 */
import { useRouter } from 'vue-router';

import useDataStore from '@/store/DataStore';

const router = useRouter();
const dataStore = useDataStore();

const { account } = defineProps(["account"]);

function goToAccountPage() {
    dataStore.expireData();
    router.push(`/accounts/${account.id}`).then(dataStore.resetData);
}
</script>

<template>
    <li>
        <a class="ubuntu-regular" @click="goToAccountPage">
            {{ account.name }}
        </a>
    </li>
</template>