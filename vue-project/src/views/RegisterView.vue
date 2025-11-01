<script setup>
import useAuthenticationStore from '@/store/AuthenticationStore';
import useDataStore from '@/store/DataStore';
import { useRouter } from 'vue-router';
import { ref } from 'vue';

const authenticationStore = useAuthenticationStore();
const dataStore = useDataStore();
const router = useRouter();

const firstNameValue = ref("");
const lastNameValue = ref("");
const emailValue = ref("");
const usernameValue = ref("");
const passwordValue = ref("");
const confirmPasswordValue = ref("");

dataStore.loadAccountsAsync().then(
    () => router.push("/accounts")
);

function goToLoginAsync() {
    return router.push("/login");
}

function register() {
    authenticationStore.registerAsync({
        email: emailValue.value,
        firstName: firstNameValue.value,
        lastName: lastNameValue.value,
        username: usernameValue.value,
        password: passwordValue.value,
        confirmPasswordValue: confirmPasswordValue.value,
    })
        .then(goToLoginAsync)
        .then(dataStore.resetData);
}

function goToHomePage() {
    return router.push("/");
}

</script>

<template>
    <div class="container-fluid p-3">
        <div class="row m-0 mb-4">
            <h1 class="libre-baskerville-regular text-center p-0">Register</h1>
        </div>
        <div class="row m-0 mb-2 justify-content-center">
            <div class="col-sm-5 col-md-4 text-sm-end p-0 pe-sm-3">
                <label for="RegisterView-first-name" class="ubuntu-regular">First Name</label>
            </div>
            <div class="col-sm-5 col-md-4 p-0">
                <input id="RegisterView-first-name" class="ubuntu-regular" type="text" v-model="firstNameValue" />
            </div>
        </div>
        <div class="row m-0 mb-3 justify-content-center">
            <div class="col-sm-5 col-md-4 text-sm-end p-0 pe-sm-3">
                <label for="RegisterView-last-name" class="ubuntu-regular">Last Name</label>
            </div>
            <div class="col-sm-5 col-md-4 p-0">
                <input id="RegisterView-last-name" class="ubuntu-regular" type="text" v-model="lastNameValue" />
            </div>
        </div>
        <div class="row m-0 mb-3 justify-content-center">
            <div class="col-sm-5 col-md-4 text-sm-end p-0 pe-sm-3">
                <label for="RegisterView-email" class="ubuntu-regular">Email</label>
            </div>
            <div class="col-sm-5 col-md-4 p-0">
                <input id="RegisterView-email" class="ubuntu-regular" type="text" v-model="emailValue" />
            </div>
        </div>
        <div class="row m-0 mb-2 justify-content-center">
            <div class="col-sm-5 col-md-4 text-sm-end p-0 pe-sm-3">
                <label for="RegisterView-username" class="ubuntu-regular">Username</label>
            </div>
            <div class="col-sm-5 col-md-4 p-0">
                <input id="RegisterView-username" class="ubuntu-regular" type="text" v-model="usernameValue" />
            </div>
        </div>
        <div class="row m-0 mb-2 justify-content-center">
            <div class="col-sm-5 col-md-4 text-sm-end p-0 pe-sm-3">
                <label for="RegisterView-password" class="ubuntu-regular">Password</label>
            </div>
            <div class="col-sm-5 col-md-4 p-0">
                <input id="RegisterView-password" class="ubuntu-regular" type="password" v-model="passwordValue" />
            </div>
        </div>
        <div class="row m-0 mb-3 justify-content-center">
            <div class="col-sm-5 col-md-4 text-sm-end p-0 pe-sm-3">
                <label for="RegisterView-confirm-password" class="ubuntu-regular">Confirm Password</label>
            </div>
            <div class="col-sm-5 col-md-4 p-0">
                <input id="RegisterView-confirm-password" class="ubuntu-regular" type="password" v-model="confirmPasswordValue" />
            </div>
        </div>
        <div class="row m-0 mb-5 justify-content-center">
            <div class="col-8 col-sm-5 col-md-4 p-0">
                <input class="btn btn-lg btn-primary happy-monkey-regular" type="submit" value="Sign Up" @click="register"/>
            </div>
        </div>
        <div class="row m-0 justify-content-center">
            <div class="col-8 col-sm-5 col-md-4 p-0">
                <a class="btn btn-lg btn-link happy-monkey-regular" @click="goToHomePage">Home</a>
            </div>
        </div>
    </div>
</template>

<style scoped>
/*
#RegisterView-form {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
}
#RegisterView-form label {
    margin-top: 0.5em;
}
#RegisterView-form input {
    margin-top: 0em;
}
#RegisterView-form .btn {
    margin-top: 0.5em;
    align-self: center;
}
    */
</style>