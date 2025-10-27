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

dataStore.loadAccountsAsync().then(
    () => router.push("/accounts")
);

function goToAccountsAsync() {
    return router.push("/accounts");
}

function login() {
    authenticationStore.loginAsync({username: usernameValue.value, password: passwordValue.value})
        .then(goToAccountsAsync)
        .then(dataStore.resetData);
}

function goToHomePage() {
    return router.push("/");
}

</script>

<template>
    <h1 class="libre-baskerville-regular">Log In</h1>
    <div id="LoginView-form">
        <label for="LoginView-username" class="ubuntu-regular">Username</label>
        <input id="LoginView-username" class="ubuntu-regular" type="text" v-model="usernameValue"/>
        <label for="LoginView-password" class="ubuntu-regular">Password</label>
        <input id="LoginView-password" class="ubuntu-regular" type="password" v-model="passwordValue"/>
        <input class="btn btn-lg btn-primary happy-monkey-regular" type="submit" value="Sign In" @click="login"/>
        <a class="btn btn-lg btn-link happy-monkey-regular" @click="goToHomePage">Home</a>
    </div>
</template>

<style scoped>
#LoginView-form {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
}
#LoginView-form label {
    margin-top: 1em;
}
#LoginView-form input {
    margin-top: 0.5em;
}
#LoginView-form .btn {
    margin-top: 1em;
    align-self: center;
}
</style>