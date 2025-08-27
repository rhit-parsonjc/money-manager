<script setup>
/*
* DateItemAndTransactionsList displays a list of DateItemAndTransactions
*/
import DateItemList from './DateItemList.vue';

const { accountId, financialTransactions, criterionType, criterion } = defineProps(["accountId", "financialTransactions", "criterionType", "criterion"])
</script>

<template>
    <DateItemList
        :accountId="accountId"
        :displayBankRecords="false"
        :bankRecords="null"
        :dateAmounts="null"
        :financialTransactions="financialTransactions"
        :criterionType="criterionType"
        :criterion="criterion"
    />
</template>