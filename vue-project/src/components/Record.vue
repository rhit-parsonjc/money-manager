<script setup>
const {record} = defineProps(["record"])
</script>

<template>
    <li class="record">
        <p>{{ record.name }}</p>
        <p>${{ record.amount.toFixed(2) }}</p>
    </li>
</template>

<style scoped>
.record {
    border: 1px solid black;
    padding: 0.5em;
    margin: 0.5em;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}
</style>