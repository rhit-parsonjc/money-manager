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
    <div class="container-fluid p-3">
        <div class="row m-0 mb-3">
            <h1 class="libre-baskerville-regular text-center p-0">Log In</h1>
        </div>
        <div class="row m-0 mb-2 justify-content-center">
            <div class="col-sm-3 col-md-2 p-0 text-sm-end pe-sm-3">
                <label for="LoginView-username" class="ubuntu-regular">Username</label>
            </div>
            <div class="col-sm-5 col-md-3 p-0">
                <input id="LoginView-username" class="ubuntu-regular" type="text" v-model="usernameValue"/>
            </div>
        </div>
        <div class="row m-0 mb-3 justify-content-center">
            <div class="col-sm-3 col-md-2 p-0 text-sm-end pe-sm-3">
                <label for="LoginView-password" class="ubuntu-regular">Password</label>
            </div>
            <div class="col-sm-5 col-md-3 p-0">
                <input id="LoginView-password" class="ubuntu-regular" type="password" v-model="passwordValue"/>
            </div>
        </div>
        <div class="row m-0 mb-5 justify-content-center">
            <div class="col-6 col-sm-4 col-md-3 p-0">
                <input class="btn btn-lg btn-primary happy-monkey-regular" type="submit" value="Sign In" @click="login"/>
            </div>
        </div>
        <div class="row m-0 justify-content-center">
            <div class="col-6 col-sm-4 col-md-3 p-0">
                <a class="btn btn-lg btn-link happy-monkey-regular" @click="goToHomePage">Home</a>
            </div>
        </div>
    </div>
</template>