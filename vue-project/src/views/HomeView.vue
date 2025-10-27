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
    <h1 class="libre-baskerville-regular">Money Manager</h1>
    <p>This app allows you to keep track of financial transactions and match them with records for an account at a financial institution.</p>
    <div id="HomeView-buttons">
        <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToRegisterPage">Register</a>
        <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToLoginPage">Log In</a>
    </div>
</template>

<style scoped>
#HomeView-buttons {
    display: flex;
    flex-direction: row;
    justify-content: space-evenly;
}
</style>