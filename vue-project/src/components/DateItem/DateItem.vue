<script setup>
/*
* DateItem represents either a DateItemAndRecords
* or a DateItemAndTransactions
* Props
* - data (DateAndRecordsModel if displayBankRecords, else DateAndTransactionsModel)
* - displayBankRecords (true if DateItemAndRecords, false if DateItemAndTransactions)
*/
import { ref } from 'vue';
import useDataStore from '@/store/DataStore';

import BankRecord from '../BankRecordOrFinancialTransaction/BankRecord.vue';
import FinancialTransaction from '../BankRecordOrFinancialTransaction/FinancialTransaction.vue';
import { formatCurrency } from '@/utilities/utilities';

const dataStore = useDataStore();

const {data, displayBankRecords} = defineProps(["data", "displayBankRecords"]);
const isEditing = ref(false);

const amount = displayBankRecords ? data.amount : null;
const hasAmount = amount !== null;
const amountValue = ref(amount);

function beginAddingOrEditing() {
  isEditing.value = true;
}

function confirmAddOrEdit() {
  const newAmount = {
    year: data.dateObj.yearValue,
    month: data.dateObj.monthValue,
    day: data.dateObj.dayValue,
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
  const {yearValue, monthValue, dayValue} = data.dateObj;
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
      <div :class="{
        'DateItem-header': true,
        'DateItem-record-header': displayBankRecords,
        'DateItem-transaction-header': !displayBankRecords
      }">
        <h2 class="happy-monkey-regular DateItem-header-text">
          {{data.dateObj.format()}}
          {{ hasAmount ? ("(" + formatCurrency(amountValue) + ")") : ""}}
        </h2>
        <div v-if="displayBankRecords && !isEditing">
          <button class="ubuntu-regular" @click="beginAddingOrEditing">
            {{ hasAmount ? "Edit" : "Add" }}
          </button>
          <button class="ubuntu-regular" @click="clearAmount" v-if="hasAmount">
            Clear
          </button>
        </div>
        <div v-else-if="displayBankRecords && isEditing">
          <input class="ubuntu-regular DateItem-amount-input" type="number" v-model="amountValue">
          <button class="ubuntu-regular" @click="confirmAddOrEdit">Confirm</button>
          <button class="ubuntu-regular" @click="cancelAddOrEdit">Cancel</button>
        </div>
      </div>
      <ul class="DateItem-items" v-if="displayBankRecords">
        <BankRecord
          v-for="record of data.bankRecords"
          :key="record.id"
          :record="record"
        />
      </ul>
      <ul class="DateItem-items" v-else>
        <FinancialTransaction
          v-for="transaction of data.financialTransactions"
          :key="transaction.id"
          :transaction="transaction"
        />
      </ul>
    </li>
</template>

<style scoped>
.DateItem-header {
  padding: 0.5rem;
  margin: 0.5rem 0em;
}
@media screen and (min-width: 600px) {
  .DateItem-header {
    display: flex;
    align-items: center;
  }
}
.DateItem-header * {
  margin-right: 1rem;
}
.DateItem-record-header, .DateItem-record-header * {
  background-color: #0c0;
}
.DateItem-record-header button:focus {
  background-color: #050;
}
.DateItem-transaction-header, .DateItem-transaction-header * {
  background-color: #0cc;
}
.DateItem-transaction-header button:focus {
  background-color: #055;
}
.DateItem-header *:last-child {
  margin-right: 0rem;
}
.DateItem-amount-input {
  width: 7em;
}
.DateItem-items {
  list-style-type: none;
  padding-left: 1rem;
}
</style>