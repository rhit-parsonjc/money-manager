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
    <h1 class="libre-baskerville-regular">Profile</h1>
    <div id="ProfileView-buttons">
        <button class="btn btn-link btn-lg happy-monkey-regular" @click="goToAccountsPage">View Accounts</button>
    </div>
    <p>Username: {{ username }}</p>
</template>

<style scoped>
#ProfileView-buttons {
    display: flex;
    flex-direction: column;
    align-items: center;
}
</style>