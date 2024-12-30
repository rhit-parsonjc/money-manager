import { defineStore } from 'pinia'
import { ref } from 'vue'

import { BankRecordModel } from '@/model/BankRecordModel'
import axios from 'axios'

const baseUrl = 'http://localhost:8080/api/v1'

/*
 * This data store is responsible for keeping track of data.
 * It needs to make sure any data provided is up-to-date with the database.
 * This does not need to retrieve updates from the database.
 */

const useDataStore = defineStore('data', () => {
  /*
   * Use the following retrieval status values:
   * NONE - No data has been loaded yet
   * DONE - All data, if any, is loaded
   * LOADING - Data is currently being loaded
   * ERROR - Data could not be loaded
   */
  const data = ref(null)
  const retrievalStatus = ref('NONE')

  function loadData(relativeUrl, responseToData) {
    data.value = null
    console.log('Loading data from ' + baseUrl + relativeUrl)
    axios
      .get(baseUrl + relativeUrl)
      .then((response) => {
        data.value = responseToData(response)
        retrievalStatus.value = 'DONE'
      })
      .catch((error) => {
        console.log(error)
        retrievalStatus.value = 'ERROR'
      })
    retrievalStatus.value = 'LOADING'
  }

  function bankRecordsToData(response) {
    return response.data.map((record) => {
      const { id, name, amount, year: yearValue, month: monthValue, day: dayValue } = record
      return new BankRecordModel(id, name, yearValue, monthValue, dayValue, amount)
    })
  }

  function bankRecordToData(response) {
    const { id, name, amount, year: yearValue, month: monthValue, day: dayValue } = response.data
    return new BankRecordModel(id, name, yearValue, monthValue, dayValue, amount)
  }

  function loadRecords() {
    loadData('/bankrecord/', bankRecordsToData)
  }

  function loadRecord(id) {
    loadData(`/bankrecord/${id}`, bankRecordToData)
  }

  function deleteRecord(id) {
    axios
      .delete(`http://localhost:8080/api/v1/bankrecord/${id}`)
      .then(() => {
        data.value = null
        retrievalStatus.value = 'NONE'
      })
      .catch((error) => console.error(error))
  }

  return { data, retrievalStatus, loadRecords, loadRecord, deleteRecord }
})

export default useDataStore
