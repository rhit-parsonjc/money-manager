<script setup>
import { useRouter } from 'vue-router';
import useDataStore from '@/store/DataStore';

const router = useRouter();
const dataStore = useDataStore();

function goToRegisterPage() {
    router.push("/register");
}

function goToLoginPage() {
    router.push("/login");
}

dataStore.loadAccountsAsync().then(
    () => router.push("/accounts")
);

</script>

<template>
    <h1 class="libre-baskerville-regular" @click="goToRegisterPage">Register</h1>
    <h1 class="libre-baskerville-regular" @click="goToLoginPage">Log In</h1>
</template>