<script setup>
const {record} = defineProps(["record"])
import { formatCurrency } from '@/utilities/utilities';
const recordUrl = "/records/" + record.id;
</script>

<template>
    <li class="record">
        <RouterLink :to="recordUrl" class="libre-baskerville-regular recordText">
            {{ record.name }} ({{ formatCurrency(record.amount) }})
        </RouterLink>
    </li>
</template>

<style scoped>
.record {
    margin: 0.5rem 0em;
}
.recordText {
    font-size: 12pt;
    color: black;
    text-decoration: none;
}
.recordText:hover {
    text-decoration: underline;
}
</style>