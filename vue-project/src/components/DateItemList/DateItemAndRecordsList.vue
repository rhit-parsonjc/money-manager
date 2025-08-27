<script setup>
/*
* DateItemAndRecordsList displays a list of DateItemAndRecords
*/
import DateItemList from './DateItemList.vue';

const { accountId, bankRecords, dateAmounts, criterionType, criterion } = defineProps(["accountId", "bankRecords", "dateAmounts", "criterionType", "criterion"])
</script>

<template>
    <DateItemList
        :accountId="accountId"
        :displayBankRecords="true"
        :bankRecords="bankRecords"
        :dateAmounts="dateAmounts"
        :financialTransactions="null"
        :criterionType="criterionType"
        :criterion="criterion"
    />
</template>