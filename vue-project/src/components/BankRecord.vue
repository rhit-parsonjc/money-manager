<script setup>
import useRecordStore from '@/store/DataStore';

const {record} = defineProps(["record"])

const recordStore = useRecordStore();
</script>

<template>
    <li class="record">
        <p>{{ record.name }}</p>
        <p>${{ record.amount.toFixed(2) }}</p>
        <button @click="recordStore.deleteRecord(record.id)">Delete</button>
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