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
import { monthNames } from '@/model/DateObjectModel';

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
  }
  if (hasAmount) {
    dataStore
      .updateDateRecordAsync(newAmount.year, newAmount.month, newAmount.day, newAmount.amount)
      .then(dataStore.resetData);
  } else {
    dataStore
      .createDateRecordAsync(newAmount)
      .then(dataStore.resetData);
  }
}

function clearAmount() {
  const {yearValue, monthValue, dayValue} = dateRecord.dateObj;
  dataStore
    .deleteDateRecordAsync(yearValue, monthValue, dayValue)
    .then(dataStore.resetData);
}

function cancelAddOrEdit() {
  isEditing.value = false;
}
</script>

<template>
    <li>
      <h2 class="dateRecordDate happy-monkey-regular">
        {{dateRecord.dateObj.format()}}
        {{ hasAmount ? ("(" + formatCurrency(dateRecord.amount) + ")") : ""}}
        <button id="addEditAmount" class="happy-monkey-regular dateRecordButton" @click="beginAddingOrEditing" v-if="!isEditing">{{ hasAmount ? "Edit" : "Add" }}</button>
        <button id="deleteAmount" class="happy-monkey-regular dateRecordButton" @click="clearAmount" v-if="!isEditing && hasAmount">Clear</button>
        <input id="amountInputForm" class="happy-monkey-regular" type="number" v-model="amountValue" v-if="isEditing">
        <button id="confirmChange" class="happy-monkey-regular dateRecordButton" @click="confirmAddOrEdit" v-if="isEditing">Confirm</button>
        <button id="cancelChange" class="happy-monkey-regular dateRecordButton" @click="cancelAddOrEdit" v-if="isEditing">Cancel</button>
      </h2>
      <ul class="bankRecordsPerDate">
        <BankRecord
          v-for="record of dateRecord.records"
          :key="record.id"
          :record="record"
        />
      </ul>
    </li>
</template>

<style scoped>
.dateRecordButton {
  background-color: #0c0;
}
#amountInputForm {
  width: 10em;
  background-color: #0c0;
}
.dateRecordDate {
  text-align: left;
  font-size: 18pt;
  background-color: #0c0;
  padding: 0.25em;
  margin: 0.5rem 0em;
}
.bankRecordsPerDate {
  list-style-type: none;
  padding-left: 1rem;
}
</style>