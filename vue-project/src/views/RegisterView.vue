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
    <h1 class="libre-baskerville-regular">Register</h1>
    <div id="RegisterView-form">
        <label for="RegisterView-first-name" class="ubuntu-regular">First Name</label>
        <input id="RegisterView-first-name" class="ubuntu-regular" type="text" v-model="firstNameValue" />
        <label for="RegisterView-last-name" class="ubuntu-regular">Last Name</label>
        <input id="RegisterView-last-name" class="ubuntu-regular" type="text" v-model="lastNameValue" />
        <label for="RegisterView-email" class="ubuntu-regular">Email</label>
        <input id="RegisterView-email" class="ubuntu-regular" type="text" v-model="emailValue" />
        <label for="RegisterView-username" class="ubuntu-regular">Username</label>
        <input id="RegisterView-username" class="ubuntu-regular" type="text" v-model="usernameValue" />
        <label for="RegisterView-password" class="ubuntu-regular">Password</label>
        <input id="RegisterView-password" class="ubuntu-regular" type="password" v-model="passwordValue" />
        <label for="RegisterView-confirm-password" class="ubuntu-regular">Confirm Password</label>
        <input id="RegisterView-confirm-password" class="ubuntu-regular" type="password" v-model="confirmPasswordValue" />
        <input class="btn btn-lg btn-primary happy-monkey-regular" type="submit" value="Sign Up" @click="register"/>
        <a class="btn btn-lg btn-link happy-monkey-regular" @click="goToHomePage">Home</a>
    </div>
</template>

<style scoped>
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
</style>