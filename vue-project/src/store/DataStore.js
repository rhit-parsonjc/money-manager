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
   * NOT LOADED - No data has been loaded yet
   * LOADING - The request for data is pending
   * ERROR - The request for data has failed
   * LOADED - The request for data has loaded
   * EXPIRED - The data is no longer valid
   */
  const data = ref()
  const retrievalStatus = ref('NOT LOADED')

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

  function resetData() {
    console.log('Set data to NOT LOADED')
    retrievalStatus.value = 'NOT LOADED'
  }

  function expireData() {
    console.log('Set data to EXPIRED')
    retrievalStatus.value = 'EXPIRED'
  }

  function loadData(relativeUrl, responseToData) {
    data.value = null
    const urlToLoadFrom = baseUrl + relativeUrl
    console.log(`Loading data from ${urlToLoadFrom}`)
    axios
      .get(urlToLoadFrom)
      .then((response) => {
        data.value = responseToData(response)
        retrievalStatus.value = 'LOADED'
        console.log('Retrieved data from ' + urlToLoadFrom)
        console.log('Data =', data.value)
      })
      .catch((error) => {
        console.error(error)
        retrievalStatus.value = 'ERROR'
      })
    retrievalStatus.value = 'LOADING'
  }

  function loadRecords() {
    return loadData('/bankrecord/', bankRecordsToData)
  }

  function loadRecord(id) {
    return loadData(`/bankrecord/${id}`, bankRecordToData)
  }

  async function modifyDataAsync(promise) {
    try {
      await promise
    } catch (error) {
      console.error(error)
    }
    expireData()
  }

  function deleteRecordAsync(id) {
    return modifyDataAsync(axios.delete(`http://localhost:8080/api/v1/bankrecord/${id}`))
  }

  function updateRecordAsync(id, record) {
    return modifyDataAsync(axios.put(`http://localhost:8080/api/v1/bankrecord/${id}`, record))
  }

  function createRecordAsync(record) {
    return modifyDataAsync(axios.post(`http://localhost:8080/api/v1/bankrecord/`, record))
  }

  return {
    data,
    retrievalStatus,
    // This sets the data status to NOT LOADED
    resetData,
    // This sets the data status to EXPIRED
    expireData,
    // These load data, but do not finish when the function returns
    loadRecords,
    loadRecord,
    // These return a promise that resolves after the request finishes
    deleteRecordAsync,
    updateRecordAsync,
    createRecordAsync,
  }
})

export default useDataStore
