<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

function goToRegisterPage() {
    router.push("/register");
}

function goToLoginPage() {
    router.push("/login");
}
</script>

<template>
    <h1 class="libre-baskerville-regular" @click="goToRegisterPage">Register</h1>
    <h1 class="libre-baskerville-regular" @click="goToLoginPage">Log In</h1>
</template>