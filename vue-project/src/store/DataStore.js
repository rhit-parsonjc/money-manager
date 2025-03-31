import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'

import { BankRecordModel } from '@/model/BankRecordModel'
import { DateAmountModel } from '@/model/DateAmountModel'
import { FinancialTransactionModel } from '@/model/FinancialTransactionModel'
import { FileAttachmentModel } from '@/model/FileAttachmentModel'

const baseUrl = 'http://localhost:8080/api/v1'

/*
 * This data store is responsible for all requests to the backend.
 * This consists of data, which represents any data from the database
 * And retrievalStatus, which represents the status of the data retrieval.
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

  function fileAttachmentToData(fileAttachment) {
    const { id, name } = fileAttachment
    return new FileAttachmentModel(id, name)
  }

  function fileAttachmentsToData(fileAttachments) {
    return fileAttachments.map(fileAttachmentToData)
  }

  function bankRecordToData(record, includesTransactions) {
    const { id, name, amount, year: yearValue, month: monthValue, day: dayValue } = record
    let mappedFinancialTransactions = null
    let mappedFileAttachments = null
    if (includesTransactions) {
      const { financialTransactions, fileAttachments } = record
      mappedFinancialTransactions = financialTransactionsToData(financialTransactions, false)
      mappedFileAttachments = fileAttachmentsToData(fileAttachments)
    }
    return new BankRecordModel(
      id,
      name,
      yearValue,
      monthValue,
      dayValue,
      amount,
      mappedFinancialTransactions,
      mappedFileAttachments,
    )
  }

  function bankRecordsToData(records, includesTransactions) {
    return records.map((record) => bankRecordToData(record, includesTransactions))
  }

  function dateAmountToData(amount) {
    const { id, year: yearValue, month: monthValue, day: dayValue, amount: amountValue } = amount
    return new DateAmountModel(id, yearValue, monthValue, dayValue, amountValue)
  }

  function dateAmountsToData(amounts) {
    return amounts.map(dateAmountToData)
  }

  function financialTransactionToData(transaction, includesRecords) {
    const { id, name, amount, year: yearValue, month: monthValue, day: dayValue } = transaction
    let mappedBankRecords = null
    let mappedFileAttachments = null
    if (includesRecords) {
      const { bankRecords, fileAttachments } = transaction
      mappedBankRecords = bankRecordsToData(bankRecords, false)
      mappedFileAttachments = fileAttachmentsToData(fileAttachments)
    }
    return new FinancialTransactionModel(
      id,
      name,
      yearValue,
      monthValue,
      dayValue,
      amount,
      mappedBankRecords,
      mappedFileAttachments,
    )
  }

  function financialTransactionsToData(transactions, includesRecords) {
    return transactions.map((transaction) =>
      financialTransactionToData(transaction, includesRecords),
    )
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
            console.log('Data', response)
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
        mapFunction: (bankRecords) => bankRecordsToData(bankRecords, false),
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
        mapFunction: (bankRecords) => bankRecordsToData(bankRecords, false),
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
        mapFunction: (bankRecords) => bankRecordsToData(bankRecords, false),
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
        mapFunction: (bankRecords) => bankRecordsToData(bankRecords, false),
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
        mapFunction: (bankRecord) => bankRecordToData(bankRecord, true),
      },
    ])
  }

  function loadFinancialTransactions() {
    loadDataFromMultipleSources([
      {
        name: 'financialTransactions',
        relativeUrl: '/financialtransactions',
        mapFunction: (financialTransactions) =>
          financialTransactionsToData(financialTransactions, false),
      },
    ])
  }

  function loadFinancialTransactionsDuringYear(year) {
    loadDataFromMultipleSources([
      {
        name: 'financialTransactions',
        relativeUrl: `/financialtransactions?year=${year}`,
        mapFunction: (financialTransactions) =>
          financialTransactionsToData(financialTransactions, false),
      },
    ])
  }

  function loadFinancialTransactionsDuringMonth(year, month) {
    loadDataFromMultipleSources([
      {
        name: 'financialTransactions',
        relativeUrl: `/financialtransactions?year=${year}&month=${month}`,
        mapFunction: (financialTransactions) =>
          financialTransactionsToData(financialTransactions, false),
      },
    ])
  }

  function loadFinancialTransactionsDuringDay(year, month, day) {
    loadDataFromMultipleSources([
      {
        name: 'financialTransactions',
        relativeUrl: `/financialtransactions?year=${year}&month=${month}&day=${day}`,
        mapFunction: (financialTransactions) =>
          financialTransactionsToData(financialTransactions, false),
      },
    ])
  }

  function loadSingleFinancialTransaction(id) {
    loadDataFromMultipleSources([
      {
        name: 'financialTransaction',
        relativeUrl: `/financialtransactions/${id}`,
        mapFunction: (financialTransaction) =>
          financialTransactionToData(financialTransaction, true),
      },
    ])
  }

  function loadBankRecordAndFinancialTransactions(id) {
    loadDataFromMultipleSources([
      {
        name: 'bankRecord',
        relativeUrl: `/bankrecords/${id}`,
        mapFunction: (bankRecord) => bankRecordToData(bankRecord, true),
      },
      {
        name: 'financialTransactions',
        relativeUrl: '/financialtransactions',
        mapFunction: (financialTransactions) =>
          financialTransactionsToData(financialTransactions, false),
      },
    ])
  }

  function loadBankRecordAndFinancialTransactionsDuringYear(id, year) {
    loadDataFromMultipleSources([
      {
        name: 'bankRecord',
        relativeUrl: `/bankrecords/${id}`,
        mapFunction: (bankRecord) => bankRecordToData(bankRecord, true),
      },
      {
        name: 'financialTransactions',
        relativeUrl: `/financialtransactions?year=${year}`,
        mapFunction: (financialTransactions) =>
          financialTransactionsToData(financialTransactions, false),
      },
    ])
  }

  function loadBankRecordAndFinancialTransactionsDuringMonth(id, year, month) {
    loadDataFromMultipleSources([
      {
        name: 'bankRecord',
        relativeUrl: `/bankrecords/${id}`,
        mapFunction: (bankRecord) => bankRecordToData(bankRecord, true),
      },
      {
        name: 'financialTransactions',
        relativeUrl: `/financialtransactions?year=${year}&month=${month}`,
        mapFunction: (financialTransactions) =>
          financialTransactionsToData(financialTransactions, false),
      },
    ])
  }

  function loadBankRecordsAndFinancialTransactionsDuringDay(id, year, month, day) {
    loadDataFromMultipleSources([
      {
        name: 'bankRecord',
        relativeUrl: `/bankrecords/${id}`,
        mapFunction: (bankRecord) => bankRecordToData(bankRecord, true),
      },
      {
        name: 'financialTransactions',
        relativeUrl: `/financialtransactions?year=${year}&month=${month}&day=${day}`,
        mapFunction: (financialTransactions) =>
          financialTransactionsToData(financialTransactions, false),
      },
    ])
  }

  function loadFinancialTransactionAndBankRecords(id) {
    loadDataFromMultipleSources([
      {
        name: 'financialTransaction',
        relativeUrl: `/financialtransactions/${id}`,
        mapFunction: (financialTransaction) =>
          financialTransactionToData(financialTransaction, true),
      },
      {
        name: 'bankRecords',
        relativeUrl: '/bankrecords',
        mapFunction: (bankRecords) => bankRecordsToData(bankRecords, false),
      },
    ])
  }

  function loadFinancialTransactionAndBankRecordsDuringYear(id, year) {
    loadDataFromMultipleSources([
      {
        name: 'financialTransaction',
        relativeUrl: `/financialtransactions/${id}`,
        mapFunction: (financialTransaction) =>
          financialTransactionToData(financialTransaction, true),
      },
      {
        name: 'bankRecords',
        relativeUrl: `/bankrecords?year=${year}`,
        mapFunction: (bankRecords) => bankRecordsToData(bankRecords, false),
      },
    ])
  }

  function loadFinancialTransactionAndBankRecordsDuringMonth(id, year, month) {
    loadDataFromMultipleSources([
      {
        name: 'financialTransaction',
        relativeUrl: `/financialtransactions/${id}`,
        mapFunction: (financialTransaction) =>
          financialTransactionToData(financialTransaction, true),
      },
      {
        name: 'bankRecords',
        relativeUrl: `/bankrecords?year=${year}&month=${month}`,
        mapFunction: (bankRecords) => bankRecordsToData(bankRecords, false),
      },
    ])
  }

  function loadFinancialTransactionAndBankRecordsDuringDay(id, year, month, day) {
    loadDataFromMultipleSources([
      {
        name: 'financialTransaction',
        relativeUrl: `/financialtransactions/${id}`,
        mapFunction: (financialTransaction) =>
          financialTransactionToData(financialTransaction, true),
      },
      {
        name: 'bankRecords',
        relativeUrl: `/bankrecords?year=${year}&month=${month}&day=${day}`,
        mapFunction: (bankRecords) => bankRecordsToData(bankRecords, false),
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

  function attachFileToBankRecordAsync(file, recordId) {
    const formData = new FormData()
    formData.append('file', file)
    return modifyDataAsync(
      axios.post(`${baseUrl}/fileattachments/bankrecords/${recordId}`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      }),
    )
  }

  function attachFileToFinancialTransactionAsync(file, transactionId) {
    const formData = new FormData()
    formData.append('file', file)
    return modifyDataAsync(
      axios.post(`${baseUrl}/fileattachments/financialtransactions/${transactionId}`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      }),
    )
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

  function deleteFileAttachmentAsync(id) {
    console.log('Delete File Attachment', { id })
    return modifyDataAsync(axios.delete(`${baseUrl}/fileattachments/${id}`))
  }

  function attachRecordAndTransaction(recordId, transactionId) {
    console.log('Attach Record and Transaction', { recordId, transactionId })
    return modifyDataAsync(axios.post(`${baseUrl}/recordtransactions/${recordId}/${transactionId}`))
  }

  function detachRecordAndTransaction(recordId, transactionId) {
    console.log('Detach Record and Transaction', { recordId, transactionId })
    return modifyDataAsync(
      axios.delete(`${baseUrl}/recordtransactions/${recordId}/${transactionId}`),
    )
  }

  // Populate the data asynchronously
  async function populateDataAsync() {
    const AMOUNT_PROPORTION = 0.2
    expireData()

    // Get a random amount
    function getRandomAmount() {
      const LOW_AMOUNT = 100
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
    function getRecordCount() {
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

    // Separate a bank record into financial transactions
    function separateBankRecord(record) {
      const { year: yearValue, month: monthValue, day: dayValue, amount, name } = record
      const transactions = []
      const value = Math.random()
      if (value < 0.5) {
        const transaction = {
          year: yearValue,
          month: monthValue,
          day: dayValue,
          amount,
          name,
        }
        transactions.push(transaction)
      } else if (value < 0.75) {
        const newAmount = Math.floor(Math.random() * amount * 100) / 100
        const transactionOne = {
          year: yearValue,
          month: monthValue,
          day: dayValue,
          amount: newAmount,
          name: name + ' I',
        }
        const transactionTwo = {
          year: yearValue,
          month: monthValue,
          day: dayValue,
          amount: amount - newAmount,
          name: name + ' II',
        }
        transactions.push(transactionOne)
        transactions.push(transactionTwo)
      }
      return transactions
    }

    let bankAmount = 1000

    let daysPerMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
    const bankRecords = []
    const dateAmountPromises = []
    for (var year = 2022; year <= 2024; year++) {
      // Update the days per month for leap years
      const isLeapYear = year % 400 === 0 || (year % 100 !== 0 && year % 4 === 0)
      daysPerMonth[1] = isLeapYear ? 29 : 28

      for (var month = 1; month <= 12; month++) {
        for (var dayValue = 1; dayValue <= daysPerMonth[month - 1]; dayValue++) {
          const recordCount = getRecordCount()

          // Generate some random bank records
          for (var i = 0; i < recordCount; i++) {
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
            bankRecords.push(record)
            if (positiveAction) {
              bankAmount += amount
            } else {
              bankAmount -= amount
            }
          }

          // Generate the correct date amount
          if (Math.random() < AMOUNT_PROPORTION) {
            const amount = {
              year,
              month,
              day: dayValue,
              amount: bankAmount,
            }
            dateAmountPromises.push(axios.post(`${baseUrl}/dateamounts`, amount))
          }
        }
      }
    }
    console.log({ bankRecords })
    // Create all of the bank records in a random order.
    while (bankRecords.length > 0) {
      const randomIndex = Math.floor(Math.random() * bankRecords.length)
      const bankRecord = bankRecords.splice(randomIndex, 1)[0]
      const bankRecordResponse = await axios.post(`${baseUrl}/bankrecords`, bankRecord)
      if (!bankRecordResponse.data.success) {
        console.error('Could not create', { bankRecord })
      }
      const savedBankRecord = bankRecordResponse.data.data
      console.log({ savedBankRecord })
      const financialTransactions = separateBankRecord(bankRecord)
      for (const financialTransaction of financialTransactions) {
        const financialTransactionResponse = await axios.post(
          `${baseUrl}/financialtransactions`,
          financialTransaction,
        )
        if (!financialTransactionResponse.data.success) {
          console.error('Could not create', { financialTransaction })
        }
        const savedFinancialTransaction = financialTransactionResponse.data.data
        console.log({ savedFinancialTransaction })
        if (bankRecordResponse.data.success && financialTransactionResponse.data.success) {
          const savedBankRecordId = savedBankRecord.id
          const savedFinancialTransactionId = savedFinancialTransaction.id
          const recordTransactionConnection = await axios.post(
            `${baseUrl}/recordtransactions/${savedBankRecordId}/${savedFinancialTransactionId}`,
          )
          if (!recordTransactionConnection.data.success) {
            console.error('Could not connect', { savedBankRecordId, savedFinancialTransactionId })
          }
        }
      }
    }

    // Apply all of the date amount promises in a random order.
    while (dateAmountPromises.length > 0) {
      const randomIndex = Math.floor(Math.random() * dateAmountPromises.length)
      const dateAmountPromise = dateAmountPromises.splice(randomIndex, 1)[0]
      await dateAmountPromise
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
    loadBankRecordAndFinancialTransactions,
    loadBankRecordAndFinancialTransactionsDuringYear,
    loadBankRecordAndFinancialTransactionsDuringMonth,
    loadBankRecordsAndFinancialTransactionsDuringDay,
    loadFinancialTransactionAndBankRecords,
    loadFinancialTransactionAndBankRecordsDuringYear,
    loadFinancialTransactionAndBankRecordsDuringMonth,
    loadFinancialTransactionAndBankRecordsDuringDay,
    // These return a promise that resolves after the request finishes
    createBankRecordAsync,
    createDateAmountAsync,
    createFinancialTransactionAsync,
    attachFileToBankRecordAsync,
    attachFileToFinancialTransactionAsync,
    updateBankRecordAsync,
    updateDateAmountAsync,
    updateFinancialTransactionAsync,
    deleteBankRecordAsync,
    deleteDateAmountAsync,
    deleteFinancialTransactionAsync,
    deleteFileAttachmentAsync,
    attachRecordAndTransaction,
    detachRecordAndTransaction,
    // This populates the data
    populateDataAsync,
  }
})

export default useDataStore
