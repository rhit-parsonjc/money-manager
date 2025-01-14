<script setup>
/**
 * DataMessages displays loading and error messages when appropriate,
 * And the appropriate component for when the data is loaded
 * Props:
 * - retrievalStatus (one of the valid data statuses)
 * - loadingMessage (text)
 * - errorMessage (text)
 */
const { retrievalStatus, loadingMessage, errorMessage } = defineProps(["retrievalStatus", "loadingMessage", "errorMessage"])
</script>

<template>
    <p class="ubuntu-regular" v-if="retrievalStatus === 'LOADING'">{{ loadingMessage }}</p>
    <p class="ubuntu-regular" v-else-if="retrievalStatus === 'ERROR'">{{ errorMessage }}</p>
    <slot v-else-if="retrievalStatus === 'LOADED'"></slot>
</template>