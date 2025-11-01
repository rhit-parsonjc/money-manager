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
    <div class="row m-0 mb-3">
        <h1 class="libre-baskerville-regular text-center p-0">{{ account === null ? "Create" : "Edit" }} Account</h1>
    </div>
    <div class="row m-0 justify-content-center mb-3">
        <div class="col-sm-2 text-sm-end p-0 pe-sm-3">
            <label for="AccountForm-name" class="ubuntu-regular">Name</label>
        </div>
        <div class="col-sm-9 p-0">
            <input id="AccountForm-name" class="ubuntu-regular" type="text" v-model="accountNameValue"/>
        </div>
    </div>
    <div class="row m-0 justify-content-around">
        <div class="col-5 col-sm-3 col-md-2 p-0">
            <input class="btn btn-lg btn-primary happy-monkey-regular" type="submit" value="Confirm" @click="confirmAction" />
        </div>
        <div class="col-5 col-sm-3 col-md-2 p-0">
            <a class="btn btn-lg btn-link happy-monkey-regular" @click="returnAction">Cancel</a>
        </div>
    </div>
</template>