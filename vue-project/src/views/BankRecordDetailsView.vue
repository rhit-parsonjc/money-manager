<script setup>
import BankRecordDetails from '@/components/BankRecordDetails.vue';
import useDataStore from '@/store/DataStore';

const {recordId} = defineProps(["recordId"]);

const dataStore = useDataStore();
dataStore.loadRecord(recordId);
</script>

<template>
    <p class="ubuntu-regular" v-if="dataStore.retrievalStatus === 'LOADING'">
        Loading Record...
    </p>
    <p class="ubuntu-regular" v-else-if="dataStore.retrievalStatus === 'ERROR'">
        Could not load record
    </p>
    <BankRecordDetails
        v-else-if="dataStore.retrievalStatus === 'DONE'"
        :record="dataStore.data"/>
</template>

<style scoped>
</style>