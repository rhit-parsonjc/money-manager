<script setup>
import { ref } from 'vue';
import useDataStore from '@/store/DataStore';
import { useRouter } from 'vue-router';

const dataStore = useDataStore();
const router = useRouter();

const { account } = defineProps(["account"]);

const initialAccountName = (account === null) ? "" : account.name;
const accountNameValue = ref(initialAccountName);

function toItem() {
    return {
        name: accountNameValue.value
    };
}

function goToListAsync() {
    return router.push("/accounts");
}

function goToDetailsAsync(id) {
    return () => router.push(`/accounts/${id}`);
}

function returnAction() {
    dataStore.expireData();
    if (account === null) {
        goToListAsync().then(dataStore.resetData);
    } else {
        goToDetailsAsync(account.id)().then(dataStore.resetData);
    }
}

function confirmAction() {
    if (account === null) {
        dataStore.createAccountAsync(toItem())
            .then(goToListAsync)
            .then(dataStore.resetData)
            .catch(err => {
                if (err === 'Unauthorized') {
                    router.push('/');
                }
            });
    } else {
        dataStore.updateAccountAsync(account.id, toItem())
            .then(goToDetailsAsync(account.id))
            .then(dataStore.resetData)
            .catch(err => {
                if (err === 'Unauthorized') {
                    router.push('/');
                }
            })
    }
}

</script>

<template>
    <h1 class="libre-baskerville-regular">{{ account === null ? "Create" : "Edit" }} Account</h1>
    <div id="AccountForm-form">
        <label for="AccountForm-name" class="ubuntu-regular">Name</label>
        <input id="AccountForm-name" class="ubuntu-regular" type="text" v-model="accountNameValue"/>
        <div id="AccountForm-buttons">
            <input class="btn btn-lg btn-primary happy-monkey-regular" type="submit" value="Confirm" @click="confirmAction" />
            <a class="btn btn-lg btn-link happy-monkey-regular" @click="returnAction">Cancel</a>
        </div>
    </div>
</template>

<style scoped>
#AccountForm-form {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
}
#AccountForm-buttons {
    display: flex;
    width: 100%;
    justify-content: space-between;
    margin-top: 1em;
}
</style>