<script setup>
import { useRouter } from 'vue-router';
import useAuthenticationStore from '@/store/AuthenticationStore';

const router = useRouter();
const authenticationStore = useAuthenticationStore();

let username = authenticationStore.username;

function goToAccountsPage() {
    return router.push("/accounts");
}
</script>

<template>
    <div class="container-fluid p-3">
        <div class="row m-0 mb-3">
            <h1 class="libre-baskerville-regular text-center p0">Profile</h1>
        </div>
        <div class="row m-0 mb-3 justify-content-center">
            <div class="col-sm-6 col-md-4">
                <button class="btn btn-link btn-lg happy-monkey-regular" @click="goToAccountsPage">View Accounts</button>
            </div>
        </div>
        <div class="row m-0">
            <p><span class="fw-bold">Username:</span> {{ username }}</p>
        </div>
    </div>
</template>