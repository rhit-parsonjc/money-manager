<script setup>
import useAuthenticationStore from '@/store/AuthenticationStore';
import useDataStore from '@/store/DataStore';
import { useRouter } from 'vue-router';
import { ref } from 'vue';

const authenticationStore = useAuthenticationStore();
const dataStore = useDataStore();
const router = useRouter();

const emailValue = ref("");
const firstNameValue = ref("");
const lastNameValue = ref("");
const usernameValue = ref("");
const passwordValue = ref("");

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
    })
        .then(goToLoginAsync)
        .then(dataStore.resetData);
}

dataStore.loadAccountsAsync().then(
    () => router.push("/accounts")
);

</script>

<template>
    <h1>Register</h1>
    <p>Email:</p>
    <input v-model="emailValue"/>
    <br />
    <p>First Name:</p>
    <input v-model="firstNameValue" />
    <br />
    <p>Last Name:</p>
    <input v-model="lastNameValue" />
    <br />
    <p>Username:</p>
    <input v-model="usernameValue"/>
    <br />
    <p>Password:</p>
    <input v-model="passwordValue"/>
    <button @click="register">Confirm</button>
</template>