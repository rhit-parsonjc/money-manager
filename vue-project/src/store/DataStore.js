import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

import { BankRecordModel } from '@/model/BankRecordModel'
import { organizeRecordsByDate } from '@/model/DateRecordModel'
import axios from 'axios'

const useRecordStore = defineStore('record', () => {
  const records = ref([])
  const dateRecords = computed(() => organizeRecordsByDate(records.value))

  function loadData() {
    axios
      .get('http://localhost:8080/api/v1/bankrecord/')
      .then((response) => {
        const recordDataList = response.data
        const recordList = recordDataList.map((recordData) => {
          const { id, name, amount, year: yearValue, month: monthValue, day: dayValue } = recordData
          return new BankRecordModel(id, name, yearValue, monthValue, dayValue, amount)
        })
        records.value = recordList
      })
      .catch((error) => console.error(error))
  }

  function deleteRecord(id) {
    axios
      .delete(`http://localhost:8080/api/v1/bankrecord/${id}`)
      .then(() => {
        loadData()
      })
      .catch((error) => console.error(error))
  }

  return { dateRecords, loadData, deleteRecord }
})

export default useRecordStore
