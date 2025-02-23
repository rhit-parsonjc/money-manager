<script setup>
/*
* DateItemAndTransactionsList displays a list of DateItemAndTransactions
* Props
* - financialTransactions (FinancialTransactionModel list)
* - criterionType ("None"/"Year"/"Month"/"Day")
* - criterion (object possibly containing year, month, and day)
*/
import DateItemList from './DateItemList.vue';

const { financialTransactions, criterionType, criterion } = defineProps(["financialTransactions", "criterionType", "criterion"])
</script>

<template>
    <DateItemList
        :displayBankRecords="false"
        :bankRecords="null"
        :dateAmounts="null"
        :financialTransactions="financialTransactions"
        :criterionType="criterionType"
        :criterion="criterion"
    />
</template>