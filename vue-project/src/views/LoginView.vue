<script setup>
import useAuthenticationStore from '@/store/AuthenticationStore';
import useDataStore from '@/store/DataStore';
import { useRouter } from 'vue-router';
import { ref } from 'vue';

const authenticationStore = useAuthenticationStore();
const dataStore = useDataStore();
const router = useRouter();

const usernameValue = ref("");
const passwordValue = ref("");

function goToAccountsAsync() {
    return router.push("/accounts");
}

function login() {
    authenticationStore.loginAsync({username: usernameValue.value, password: passwordValue.value})
        .then(goToAccountsAsync)
        .then(dataStore.resetData);
}

dataStore.loadAccountsAsync().then(
    () => router.push("/accounts")
);

</script>

<template>
    <h1>Log In</h1>
    <input v-model="usernameValue"/>
    <input v-model="passwordValue"/>
    <button @click="login">Log In</button>
</template>