import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'

import { BankRecordModel } from '@/model/BankRecordModel'
import { DateAmountModel } from '@/model/DateAmountModel'

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

  function bankRecordToData(record) {
    const { id, name, amount, year: yearValue, month: monthValue, day: dayValue } = record
    return new BankRecordModel(id, name, yearValue, monthValue, dayValue, amount)
  }

  function bankRecordsToData(records) {
    return records.map(bankRecordToData)
  }

  function dateAmountToData(amount) {
    const { id, year: yearValue, month: monthValue, day: dayValue, amount: amountValue } = amount
    return new DateAmountModel(id, yearValue, monthValue, dayValue, amountValue)
  }

  function dateAmountsToData(amounts) {
    return amounts.map(dateAmountToData)
  }

  function resetData() {
    console.log('Set data to NOT LOADED')
    retrievalStatus.value = 'NOT LOADED'
  }

  function expireData() {
    console.log('Set data to EXPIRED')
    retrievalStatus.value = 'EXPIRED'
  }

  function loadDataFromMultipleSources(sources) {
    data.value = {}
    const promises = []
    for (const source of sources) {
      promises.push(axios.get(baseUrl + source.relativeUrl))
    }
    Promise.all(promises)
      .then((responses) => {
        console.log(responses)
        for (const i in responses) {
          const response = responses[i]
          const source = sources[i]
          data.value[source.name] = source.mapFunction(response.data)
        }
        console.log('Set data to LOADED')
        retrievalStatus.value = 'LOADED'
      })
      .catch((error) => {
        console.error(error)
        retrievalStatus.value = 'ERROR'
      })
    retrievalStatus.value = 'LOADING'
  }

  function loadDateAndBankRecords() {
    loadDataFromMultipleSources([
      {
        name: 'bankRecords',
        relativeUrl: '/bankrecords',
        mapFunction: bankRecordsToData,
      },
      {
        name: 'dateAmounts',
        relativeUrl: '/dateamounts',
        mapFunction: dateAmountsToData,
      },
    ])
  }

  function loadDateAndBankRecordsDuringYear(year) {
    loadDataFromMultipleSources([
      {
        name: 'bankRecords',
        relativeUrl: `/bankrecords?year=${year}`,
        mapFunction: bankRecordsToData,
      },
      {
        name: 'dateAmounts',
        relativeUrl: `/dateamounts?year=${year}`,
        mapFunction: dateAmountsToData,
      },
    ])
  }

  function loadDateAndBankRecordsDuringMonth(year, month) {
    loadDataFromMultipleSources([
      {
        name: 'bankRecords',
        relativeUrl: `/bankrecords?year=${year}&month=${month}`,
        mapFunction: bankRecordsToData,
      },
      {
        name: 'dateAmounts',
        relativeUrl: `/dateamounts?year=${year}&month=${month}`,
        mapFunction: dateAmountsToData,
      },
    ])
  }

  function loadDateAndBankRecordsDuringDay(year, month, day) {
    loadDataFromMultipleSources([
      {
        name: 'bankRecords',
        relativeUrl: `/bankrecords?year=${year}&month=${month}&day=${day}`,
        mapFunction: bankRecordsToData,
      },
      {
        name: 'dateAmounts',
        relativeUrl: `/dateamounts?year=${year}&month=${month}&day=${day}`,
        mapFunction: dateAmountsToData,
      },
    ])
  }

  function loadSingleBankRecord(id) {
    loadDataFromMultipleSources([
      {
        name: 'bankRecord',
        relativeUrl: `/bankrecords/${id}`,
        mapFunction: bankRecordToData,
      },
    ])
  }

  async function modifyDataAsync(promise) {
    try {
      await promise
    } catch (error) {
      console.error(error)
    }
    expireData()
  }

  function deleteBankRecordAsync(id) {
    return modifyDataAsync(axios.delete(`${baseUrl}/bankrecords/${id}`))
  }

  function updateBankRecordAsync(id, record) {
    return modifyDataAsync(axios.put(`${baseUrl}/bankrecords/${id}`, record))
  }

  function createBankRecordAsync(record) {
    return modifyDataAsync(axios.post(`${baseUrl}/bankrecords/`, record))
  }

  function deleteDateAmountAsync(yearValue, monthValue, dayValue) {
    return modifyDataAsync(
      axios.delete(`${baseUrl}/dateamounts/${yearValue}/${monthValue}/${dayValue}`),
    )
  }

  function updateDateAmountAsync(yearValue, monthValue, dayValue, amount) {
    return modifyDataAsync(
      axios.put(`${baseUrl}/dateamounts/${yearValue}/${monthValue}/${dayValue}`, { amount }),
    )
  }

  function createDateAmountAsync(dateRecord) {
    return modifyDataAsync(axios.post(`${baseUrl}/dateamounts`, dateRecord))
  }

  return {
    data,
    retrievalStatus,
    // This sets the data status to NOT LOADED
    resetData,
    // This sets the data status to EXPIRED
    expireData,
    // These load data, but do not finish when the function returns
    loadDateAndBankRecords,
    loadDateAndBankRecordsDuringYear,
    loadDateAndBankRecordsDuringMonth,
    loadDateAndBankRecordsDuringDay,
    loadSingleBankRecord,
    // These return a promise that resolves after the request finishes
    deleteBankRecordAsync,
    updateBankRecordAsync,
    createBankRecordAsync,
    deleteDateAmountAsync,
    updateDateAmountAsync,
    createDateAmountAsync,
  }
})

export default useDataStore
