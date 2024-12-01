import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

import { BankRecordModel } from '@/model/BankRecordModel'
import { organizeRecordsByDate } from '@/model/DateRecordModel'

const useRecordStore = defineStore('record', () => {
  const records = ref([
    new BankRecordModel('Streaming Subscription', 2024, 10, 10, 25.0),
    new BankRecordModel('Vending Machine Snack', 2024, 10, 6, 5.0),
    new BankRecordModel('Tax Refund', 2024, 10, 7, -1.5),
    new BankRecordModel('Dinner', 2024, 10, 7, 15.5),
  ])
  const dateRecords = computed(() => organizeRecordsByDate(records.value))

  return { dateRecords }
})

export default useRecordStore
