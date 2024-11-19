<script setup>
function createDate(yearValue, monthValue, dayValue) {
  const dateObj = new Date();
  dateObj.setUTCFullYear(yearValue, monthValue, dayValue);
  dateObj.setUTCHours(0, 0, 0, 0);
  return dateObj;
}
function formatDate(dateObj) {
  const monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
  const dayNames = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
  const yearIndex = dateObj.getUTCFullYear();
  const monthIndex = dateObj.getUTCMonth();
  const dateIndex = dateObj.getUTCDate();
  const dayIndex = dateObj.getUTCDay();
  return dayNames[dayIndex] + ", " + monthNames[monthIndex] + " " + dateIndex + ", " + yearIndex;
}
function areDatesEqual(dateOne, dateTwo) {
  return dateOne.getTime() === dateTwo.getTime();
}
const records = [
  {
    name: "Streaming Subscription",
    date: createDate(2024, 10, 10),
    amount: 25.00,
  },
  {
    name: "Vending Machine Snack",
    date: createDate(2024, 10, 6),
    amount: 5.00,
  },
  {
    name: "Tax Refund",
    date: createDate(2024, 10, 7),
    amount: -1.50,
  },
  {
    name: "Dinner",
    date: createDate(2024, 10, 7),
    amount: 15.50,
  },
];
records.sort((a, b) => a.date - b.date);
const firstDate = records[0].date;
const lastDate = records[records.length - 1].date;
const recordsPerDate = [];
let recordIndex = 0;
for (const dateObj = new Date(firstDate); dateObj <= lastDate; dateObj.setDate(dateObj.getDate() + 1)) {
  const newRecordPerDate = {date: new Date(dateObj), records: []};
  while (recordIndex < records.length && areDatesEqual(records[recordIndex].date, dateObj)) {
    const record = records[recordIndex];
    newRecordPerDate.records.push({name: record.name, amount: record.amount});
    recordIndex++;
  }
  recordsPerDate.push(newRecordPerDate);
}
console.log(recordsPerDate);
</script>

<template>
  <h1>Records</h1>
  <ul>
    <li v-for="recordPerDate of recordsPerDate">
      <h2 class="date">{{formatDate(recordPerDate.date)}}</h2>
      <li v-for="record of recordPerDate.records" class="record">
        <p>{{ record.name }}</p>
        <p>${{ record.amount.toFixed(2) }}</p>
      </li>
    </li>
  </ul>
</template>

<style scoped>
* {
  box-sizing: border-box;
}
p, h1, h2 {
  margin: 0;
}
ul {
  list-style-type: none;
  padding-left: 0em;
}
.date {
  text-align: center;
}
.record {
  border: 1px solid black;
  padding: 0.5em;
  margin: 0.5em;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
</style>