<script setup>
import { ref, computed } from 'vue';
/*
* DateFilter represents a filter that specifies a date range to load data from.
* The filter type is specified by criterionTypeValue
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
    <div class="row align-items-center justify-content-center m-0">
        <div class="col-5 col-sm-3 col-md-2 p-0 d-inline-flex justify-content-end">
            <label for="DateFilter-filter-type" class="ubuntu-regular">Filter By:</label>
        </div>
        <div class="col-4 col-sm-3 col-md-2 p-2">
            <select id="DateFilter-filter-type" class="ubuntu-regular" v-model="criterionTypeValue">
                <option key="None" class="ubuntu-regular">None</option>
                <option key="Year" class="ubuntu-regular">Year</option>
                <option key="Month" class="ubuntu-regular">Month</option>
                <option key="Day" class="ubuntu-regular">Day</option>
            </select>
        </div>
        <div class="col-6 col-sm-3 col-md-2 p-2" v-if="criterionTypeValue === 'Year'">
            <input type="number" class="ubuntu-regular DateFilter-year-input" v-model="yearValue">
        </div>
        <div class="col-10 col-sm-6 col-md-4 p-2" v-if="criterionTypeValue === 'Month'">
            <input type="month" class="ubuntu-regular" v-model="monthValue">
        </div>
        <div class="col-9 col-sm-5 col-md-4 p-2" v-if="criterionTypeValue === 'Day'">
            <input type="date" class="ubuntu-regular" v-model="dayValue">
        </div>
        <div class="col-8 col-sm-4 col-md-3 p-2">
            <button class="btn btn-lg btn-secondary ubuntu-regular DateFilter-apply-filter" @click="$emit('applyFilter', getCriterionInfo())" :disabled="criterionInvalid">Apply Filter</button>
        </div>
    </div>
</template>