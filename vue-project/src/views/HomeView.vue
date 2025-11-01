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
    <div class="container-fluid p-3">
        <div class="row m-0 mb-3">
            <h1 class="libre-baskerville-regular text-center p-0">Money Manager</h1>
        </div>
        <div class="row m-0 mb-3">
            <p class="p-0">This app allows you to keep track of financial transactions and match them with records for an account at a financial institution.</p>
        </div>
        <div class="row m-0 justify-content-around">
            <div class="col-4 col-sm-3 col-md-2 p-0">
                <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToRegisterPage">Register</a>
            </div>
            <div class="col-4 col-sm-3 col-md-2 p-0">
                <a class="btn btn-link btn-lg happy-monkey-regular" @click="goToLoginPage">Log In</a>
            </div>
        </div>
    </div>
</template>