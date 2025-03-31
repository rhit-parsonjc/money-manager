<script setup>
import { ref, computed } from 'vue';
/*
* DateFilter represents a filter that specifies a date range to load data from.
* The filter type is specified by criterionTypeValue, which can be either:
* - None
* - Year
* - Month
* - Day
*/

const yearValue = ref();
const monthValue = ref();
const dayValue = ref();
const criterionTypeValue = ref("None");

const criterionInvalid = computed(() => {
  switch (criterionTypeValue.value) {
    case "None":
      return false;
    case "Year":
      return yearValue.value === undefined;
    case "Month":
      return monthValue.value === undefined;
    case "Day":
      return dayValue.value === undefined;
    default:
      return true;
  }
})

function getCriterionInfo() {
    let parts = undefined;
    switch (criterionTypeValue.value) {
        case "None":
            return {
                criterionType: criterionTypeValue.value,
                criterion: {}
            };
        case "Year":
            if (yearValue.value === undefined) return null
            return {
                criterionType: criterionTypeValue.value,
                criterion: {
                    year: yearValue.value
                }
            }
        case "Month":
            if (monthValue.value === undefined) return null
            parts = monthValue.value.split("-")
            return {
                criterionType: criterionTypeValue.value,
                criterion: {
                    year: parseInt(parts[0]),
                    month: parseInt(parts[1])
                }
            }
        case "Day":
            if (dayValue.value === undefined) return null
            parts = dayValue.value.split("-");
            return {
                criterionType: criterionTypeValue.value,
                criterion: {
                    year: parseInt(parts[0]),
                    month: parseInt(parts[1]),
                    day: parseInt(parts[2])
                }
            }
    }
}

</script>

<template>
    <div class="DateFilter-filter">
        <p class="ubuntu-regular">Filter By:</p>
        <select class="ubuntu-regular" v-model="criterionTypeValue">
            <option key="None" class="ubuntu-regular">None</option>
            <option key="Year" class="ubuntu-regular">Year</option>
            <option key="Month" class="ubuntu-regular">Month</option>
            <option key="Day" class="ubuntu-regular">Day</option>
        </select>
        <input type="number" class="ubuntu-regular DateFilter-year-input" v-model="yearValue" v-if="criterionTypeValue === 'Year'">
        <input type="month" class="ubuntu-regular" v-model="monthValue" v-if="criterionTypeValue === 'Month'">
        <input type="date" class="ubuntu-regular" v-model="dayValue" v-if="criterionTypeValue === 'Day'">
        <button class="ubuntu-regular DateFilter-apply-filter" @click="$emit('applyFilter', getCriterionInfo())" :disabled="criterionInvalid">Apply Filter</button>
    </div>
</template>

<style scoped>
.DateFilter-filter {
  display: flex;
}
.DateFilter-filter * {
  margin-right: 0.5em;
}
.DateFilter-filter *:last-child {
  margin-right: 0em;
}
.DateFilter-year-input {
  width: 5em;
}
.DateFilter-apply-filter {
  background-color: white;
}
</style>