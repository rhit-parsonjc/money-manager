<script setup>
/*
* DateItemAndRecordsList displays a list of DateItemAndRecords
* Props
* - bankRecords (BankRecordModel list)
* - dateAmounts (DateAmountModel list)
* - criterionType ("None"/"Year"/"Month"/"Day")
* - criterion (object possibly containing year, month, and day)
*/
import DateItemList from './DateItemList.vue';

const { bankRecords, dateAmounts, criterionType, criterion } = defineProps(["bankRecords", "dateAmounts", "criterionType", "criterion"])
</script>

<template>
    <DateItemList
        :displayBankRecords="true"
        :bankRecords="bankRecords"
        :dateAmounts="dateAmounts"
        :financialTransactions="null"
        :criterionType="criterionType"
        :criterion="criterion"
    />
</template>