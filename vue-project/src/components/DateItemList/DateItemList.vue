<script setup>
/*
* DateItemList represents either a DateItemAndRecordsList
* or a DateItemAndTransactionsList
* Props
* - displayBankRecords (true if DateItemAndRecordsList, false if DateItemAndTransactionsList)
* - bankRecords (BankRecordModel list)
* - dateAmounts (DateAmountModel list)
* - financialTransactions (FinancialTransactionModel list)
* - criterionType ("None"/"Year"/"Month"/"Day")
* - criterion (object possibly containing year, month, and day)
*/
import { computed } from "vue";

import { organizeRecordsByDate } from "@/model/DateAndRecordsModel";
import { organizeTransactionsByDate } from "@/model/DateAndTransactionsModel";
import { DateObjectModel, getFirstDayOfYear, getLastDayOfYear, getFirstDayOfMonth, getLastDayOfMonth } from "@/model/DateObjectModel";
import DateItem from "../DateItem/DateItem.vue";

const { displayBankRecords, bankRecords, dateAmounts, financialTransactions, criterionType, criterion } = defineProps(["displayBankRecords", "bankRecords", "dateAmounts", "financialTransactions", "criterionType", "criterion"]);

const dateItems = computed(() => {
  let startDate = null;
  let endDate = null;
  switch (criterionType) {
    case "Year":
      startDate = getFirstDayOfYear(criterion.year);
      endDate = getLastDayOfYear(criterion.year);
      break;
    case "Month":
      startDate = getFirstDayOfMonth(criterion.year, criterion.month);
      endDate = getLastDayOfMonth(criterion.year, criterion.month);
      break;
    case "Day":
      startDate = new DateObjectModel(criterion.year, criterion.month, criterion.day);
      endDate = new DateObjectModel(criterion.year, criterion.model, criterion.day);
      break;
  }
  if (displayBankRecords) {
    return organizeRecordsByDate(bankRecords, dateAmounts, startDate, endDate)
  } else {
    return organizeTransactionsByDate(financialTransactions, startDate, endDate)
  }
})
</script>

<template>
    <ul class="DateItemList-list">
      <DateItem
        v-for="dateItem of dateItems"
        :data="dateItem"
        :displayBankRecords="displayBankRecords"
        :key="dateItem.dateObj.toNumber()"
      />
    </ul>
</template>

<style scoped>
.DateItemList-list {
  list-style-type: none;
  padding-left: 0rem;
}
</style>