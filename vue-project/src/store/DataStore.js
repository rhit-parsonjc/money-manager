import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'

import { BankRecordModel } from '@/model/BankRecordModel'
import { DateAmountModel } from '@/model/DateAmountModel'
import { FinancialTransactionModel } from '@/model/FinancialTransactionModel'

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

  function financialTransactionToData(transaction) {
    const { id, name, amount, year: yearValue, month: monthValue, day: dayValue } = transaction
    return new FinancialTransactionModel(id, name, yearValue, monthValue, dayValue, amount)
  }

  function financialTransactionsToData(transactions) {
    return transactions.map(financialTransactionToData)
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
        let errorMessage = null
        for (const response of responses) {
          if (!response.data.success) {
            errorMessage = response.data.data
          }
        }
        if (errorMessage !== null) {
          console.error(errorMessage)
          retrievalStatus.value = 'ERROR'
        } else {
          for (const i in responses) {
            const response = responses[i]
            const source = sources[i]
            data.value[source.name] = source.mapFunction(response.data.data)
          }
          console.log('Set data to LOADED')
          console.log(data.value)
          retrievalStatus.value = 'LOADED'
        }
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

  function loadFinancialTransactions() {
    loadDataFromMultipleSources([
      {
        name: 'financialTransactions',
        relativeUrl: '/financialtransactions',
        mapFunction: financialTransactionsToData,
      },
    ])
  }

  function loadFinancialTransactionsDuringYear(year) {
    loadDataFromMultipleSources([
      {
        name: 'financialTransactions',
        relativeUrl: `/financialtransactions?year=${year}`,
        mapFunction: financialTransactionsToData,
      },
    ])
  }

  function loadFinancialTransactionsDuringMonth(year, month) {
    loadDataFromMultipleSources([
      {
        name: 'financialTransactions',
        relativeUrl: `/financialtransactions?year=${year}&month=${month}`,
        mapFunction: financialTransactionsToData,
      },
    ])
  }

  function loadFinancialTransactionsDuringDay(year, month, day) {
    loadDataFromMultipleSources([
      {
        name: 'financialTransactions',
        relativeUrl: `/financialtransactions?year=${year}&month=${month}&day=${day}`,
        mapFunction: financialTransactionsToData,
      },
    ])
  }

  function loadSingleFinancialTransaction(id) {
    loadDataFromMultipleSources([
      {
        name: 'financialTransaction',
        relativeUrl: `/financialtransactions/${id}`,
        mapFunction: financialTransactionToData,
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

  function createBankRecordAsync(bankRecord) {
    console.log('Create Bank Record', { bankRecord })
    return modifyDataAsync(axios.post(`${baseUrl}/bankrecords`, bankRecord))
  }

  function createDateAmountAsync(dateAmount) {
    console.log('Create Date Amount', { dateAmount })
    return modifyDataAsync(axios.post(`${baseUrl}/dateamounts`, dateAmount))
  }

  function createFinancialTransactionAsync(financialTransaction) {
    console.log('Create Financial Transactions', { financialTransaction })
    return modifyDataAsync(axios.post(`${baseUrl}/financialtransactions`, financialTransaction))
  }

  function updateBankRecordAsync(id, bankRecord) {
    console.log('Update Bank Record', { id, bankRecord })
    return modifyDataAsync(axios.put(`${baseUrl}/bankrecords/${id}`, bankRecord))
  }

  function updateDateAmountAsync(yearValue, monthValue, dayValue, dateAmount) {
    console.log('Update Date Amount', { yearValue, monthValue, dayValue, dateAmount })
    return modifyDataAsync(
      axios.put(`${baseUrl}/dateamounts/${yearValue}/${monthValue}/${dayValue}`, {
        amount: dateAmount,
      }),
    )
  }

  function updateFinancialTransactionAsync(id, financialTransaction) {
    console.log('Update Financial Transaction', { id, financialTransaction })
    return modifyDataAsync(
      axios.put(`${baseUrl}/financialtransactions/${id}`, financialTransaction),
    )
  }

  function deleteBankRecordAsync(id) {
    console.log('Delete Bank Record', { id })
    return modifyDataAsync(axios.delete(`${baseUrl}/bankrecords/${id}`))
  }

  function deleteDateAmountAsync(yearValue, monthValue, dayValue) {
    console.log('Delete Date Amount', { yearValue, monthValue, dayValue })
    return modifyDataAsync(
      axios.delete(`${baseUrl}/dateamounts/${yearValue}/${monthValue}/${dayValue}`),
    )
  }

  function deleteFinancialTransactionAsync(id) {
    console.log('Delete Financial Transaction', { id })
    return modifyDataAsync(axios.delete(`${baseUrl}/financialtransactions/${id}`))
  }

  // Populate the data asynchronously
  async function populateDataAsync() {
    expireData()

    // Get a random amount
    function getRandomAmount() {
      const LOW_AMOUNT = 1000
      const HIGH_AMOUNT = 9999
      const RANGE = HIGH_AMOUNT - LOW_AMOUNT + 1
      return (Math.floor(Math.random() * RANGE) + LOW_AMOUNT) / 100
    }

    // Return a random element of the array
    function selectRandom(arr) {
      const index = Math.floor(Math.random() * arr.length)
      return arr[index]
    }

    // Return an action that adds money to the account
    function getPositiveAction() {
      return selectRandom(['Tax Refund', 'Gift', 'Interest', 'Income'])
    }

    // Return an action that removes money from the account
    function getNegativeAction() {
      return selectRandom([
        'Breakfast',
        'Lunch',
        'Dinner',
        'Movie',
        'Video Game',
        'Souvenir',
        'Plush',
        'Groceries',
        'Rent',
      ])
    }

    // Determine the number of financial transactions for a given day
    function getTransactionCount() {
      const value = Math.random()
      if (value < 0.9) {
        return 0
      } else if (value < 0.95) {
        return 1
      } else if (value < 0.98) {
        return 2
      } else {
        return 3
      }
    }

    let bankAmount = 1000

    let daysPerMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
    for (var year = 2022; year <= 2024; year++) {
      // Update the days per month for leap years
      const isLeapYear = year % 400 === 0 || (year % 100 !== 0 && year % 4 === 0)
      daysPerMonth[1] = isLeapYear ? 29 : 28

      for (var month = 1; month <= 12; month++) {
        for (var dayValue = 1; dayValue <= daysPerMonth[month - 1]; dayValue++) {
          const transactionCount = getTransactionCount()

          for (var i = 0; i < transactionCount; i++) {
            const amount = getRandomAmount()
            const positiveAction = Math.random() < 0.5
            const name = positiveAction ? getPositiveAction() : getNegativeAction()
            const record = {
              year,
              month,
              day: dayValue,
              amount: positiveAction ? amount : -amount,
              name,
            }
            await axios.post(`${baseUrl}/bankrecords`, record)
            if (positiveAction) {
              bankAmount += amount
            } else {
              bankAmount -= amount
            }
          }

          if (Math.random() < 0.1) {
            const amount = {
              year,
              month,
              day: dayValue,
              amount: bankAmount,
            }
            await axios.post(`${baseUrl}/dateamounts`, amount)
          }
        }
      }
    }
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
    loadFinancialTransactions,
    loadFinancialTransactionsDuringYear,
    loadFinancialTransactionsDuringMonth,
    loadFinancialTransactionsDuringDay,
    loadSingleFinancialTransaction,
    // These return a promise that resolves after the request finishes
    createBankRecordAsync,
    createDateAmountAsync,
    createFinancialTransactionAsync,
    updateBankRecordAsync,
    updateDateAmountAsync,
    updateFinancialTransactionAsync,
    deleteBankRecordAsync,
    deleteDateAmountAsync,
    deleteFinancialTransactionAsync,
    // This populates the data
    populateDataAsync,
  }
})

export default useDataStore
