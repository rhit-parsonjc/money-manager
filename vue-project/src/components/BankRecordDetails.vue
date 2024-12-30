<script setup>
const {record} = defineProps(["record"])
</script>

<template>
    <h1>{{ record.name }}</h1>
    <p>{{ record.dateObj.format() }}</p>
    <p>Amount: ${{ record.amount.toFixed(2) }}</p>
</template>

<style scoped>
</style>