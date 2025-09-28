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

function goToLoginAsync() {
    return router.push("/login");
}

function register() {
    authenticationStore.registerAsync({username: usernameValue.value, password: passwordValue.value})
        .then(goToLoginAsync)
        .then(dataStore.resetData);
}

dataStore.loadAccountsAsync().then(
    () => router.push("/accounts")
);

</script>

<template>
    <h1>Register</h1>
    <input v-model="usernameValue"/>
    <input v-model="passwordValue"/>
    <button @click="register">Confirm</button>
</template>