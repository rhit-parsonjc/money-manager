<script setup>
import { DataStatus } from '@/store/DataStore';

/**
 * DataMessages displays loading and error messages when appropriate,
 * And the appropriate component for when the data is loaded
 */
const { retrievalStatus, loadingMessage, errorMessage } = defineProps(["retrievalStatus", "loadingMessage", "errorMessage"]);
</script>

<template>
    <p class="ubuntu-regular" v-if="retrievalStatus === DataStatus.LOADING">{{ loadingMessage }}</p>
    <p class="ubuntu-regular" v-else-if="retrievalStatus === DataStatus.ERROR">{{ errorMessage }}</p>
    <slot v-else-if="retrievalStatus === DataStatus.LOADED"></slot>
</template>