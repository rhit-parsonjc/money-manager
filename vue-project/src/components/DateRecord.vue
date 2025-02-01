<script setup>
/**
 * DateRecord displays a single date record, comprised of
 * a date plus a list of bank records.
 * Props:
 * - dateRecord (type DateRecordModel)
 */
import { ref } from 'vue';

import BankRecord from './BankRecord.vue';
import { formatCurrency } from '@/utilities/utilities';
import useDataStore from '@/store/DataStore';

const dataStore = useDataStore();

const {dateRecord} = defineProps(["dateRecord"]);
const isEditing = ref(false);

const amount = dateRecord.amount;
const hasAmount = amount !== null;
const amountValue = ref(dateRecord.amount);

function beginAddingOrEditing() {
  isEditing.value = true;
}

function confirmAddOrEdit() {
  const newAmount = {
    year: dateRecord.dateObj.yearValue,
    month: dateRecord.dateObj.monthValue,
    day: dateRecord.dateObj.dayValue,
    amount: amountValue.value,
  };
  if (hasAmount) {
    dataStore
      .updateDateAmountAsync(newAmount.year, newAmount.month, newAmount.day, newAmount.amount)
      .then(dataStore.resetData);
  } else {
    dataStore
      .createDateAmountAsync(newAmount)
      .then(dataStore.resetData);
  }
}

function clearAmount() {
  const {yearValue, monthValue, dayValue} = dateRecord.dateObj;
  dataStore
    .deleteDateAmountAsync(yearValue, monthValue, dayValue)
    .then(dataStore.resetData);
}

function cancelAddOrEdit() {
  isEditing.value = false;
}
</script>

<template>
    <li>
      <div class="DateRecord-header">
        <h2 class="happy-monkey-regular DateRecord-header-text">
          {{dateRecord.dateObj.format()}}
          {{ hasAmount ? ("(" + formatCurrency(dateRecord.amount) + ")") : ""}}
        </h2>
        <button class="ubuntu-regular" @click="beginAddingOrEditing" v-if="!isEditing">{{ hasAmount ? "Edit" : "Add" }}</button>
        <button class="ubuntu-regular" @click="clearAmount" v-if="!isEditing && hasAmount">Clear</button>
        <input class="ubuntu-regular DateRecord-amount-input" type="number" v-model="amountValue" v-if="isEditing">
        <button class="ubuntu-regular" @click="confirmAddOrEdit" v-if="isEditing">Confirm</button>
        <button class="ubuntu-regular" @click="cancelAddOrEdit" v-if="isEditing">Cancel</button>
      </div>
      <ul class="DateRecord-bank-records">
        <BankRecord
          v-for="record of dateRecord.records"
          :key="record.id"
          :record="record"
        />
      </ul>
    </li>
</template>

<style scoped>
.DateRecord-header {
  background-color: #0c0;
  padding: 0.5rem;
  margin: 0.5rem 0em;
}
@media screen and (min-width: 600px) {
  .DateRecord-header {
    display: flex;
    align-items: center;
  }
}
.DateRecord-header * {
  background-color: #0c0;
  margin-right: 1rem;
}
.DateRecord-header button:focus {
  background-color: #050;
}
.DateRecord-header *:last-child {
  margin-right: 0rem;
}
.DateRecord-amount-input {
  width: 7em;
}
.DateRecord-bank-records {
  list-style-type: none;
  padding-left: 1rem;
}
</style>