<script setup>
import { DataStatus } from '@/store/DataStore';

/**
 * DataMessages displays loading and error messages when appropriate,
 * And the appropriate component for when the data is loaded
 */
const { retrievalStatus, loadingMessage, errorMessage } = defineProps(["retrievalStatus", "loadingMessage", "errorMessage"]);
</script>

<template>
    <div class="div position-absolute top-50 start-50 translate-middle" v-if="retrievalStatus === DataStatus.LOADING">
        <div class="spinner-border" role="status">
            <span class="visually-hidden">{{ loadingMessage }}</span>
        </div>
    </div>
    <div class="alert alert-info ubuntu-regular" role="alert" v-else-if="retrievalStatus === DataStatus.ERROR">
        {{ errorMessage }}
    </div>
    <slot v-else-if="retrievalStatus === DataStatus.LOADED"></slot>
</template>