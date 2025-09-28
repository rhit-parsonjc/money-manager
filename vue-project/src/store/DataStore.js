import { defineStore } from 'pinia'
import axios from 'axios'

import useAuthenticationStore from './AuthenticationStore'

import { BankRecordModel } from '@/model/BankRecordModel'
import { DateAmountModel } from '@/model/DateAmountModel'
import { FinancialTransactionModel } from '@/model/FinancialTransactionModel'
import { FileAttachmentModel } from '@/model/FileAttachmentModel'
import { AccountModel } from '@/model/AccountModel'

const baseUrl = 'http://localhost:8080/api/v1'

export const DataStatus = {
  NOT_LOADED: 1, // No data has been loaded yet
  LOADING: 2, // The request for data is pending
  ERROR: 3, // The request for data has failed
  LOADED: 4, // The request for data has loaded
  EXPIRED: 5, // The data is no longer valid
}

/*
 * These functions below convert data retrieved from the database
 * Into the format of one or more model classes
 */

function toAccountModel(account) {
  const { id, name } = account
  return new AccountModel(id, name)
}

function toAccountModels(accounts) {
  return accounts.map(toAccountModel)
}

function toBankRecordModel(bankRecord) {
  const { id, name, yearValue, monthValue, dayValue, amount } = bankRecord
  return new BankRecordModel(id, name, yearValue, monthValue, dayValue, amount, null, null)
}

function toBankRecordModelFull(bankRecord) {
  const {
    id,
    name,
    yearValue,
    monthValue,
    dayValue,
    amount,
    financialTransactions,
    fileAttachments,
  } = bankRecord
  const financialTransactionModels = toFinancialTransactionModels(financialTransactions)
  const fileAttachmentModels = toFileAttachmentModels(fileAttachments)
  return new BankRecordModel(
    id,
    name,
    yearValue,
    monthValue,
    dayValue,
    amount,
    financialTransactionModels,
    fileAttachmentModels,
  )
}

function toBankRecordModels(bankRecords) {
  return bankRecords.map(toBankRecordModel)
}

function toFinancialTransactionModel(financialTransaction) {
  const { id, name, yearValue, monthValue, dayValue, amount } = financialTransaction
  return new FinancialTransactionModel(
    id,
    name,
    yearValue,
    monthValue,
    dayValue,
    amount,
    null,
    null,
  )
}

function toFinancialTransactionModelFull(financialTransaction) {
  const { id, name, yearValue, monthValue, dayValue, amount, bankRecords, fileAttachments } =
    financialTransaction
  const bankRecordModels = toBankRecordModels(bankRecords)
  const fileAttachmentModels = toFileAttachmentModels(fileAttachments)
  return new FinancialTransactionModel(
    id,
    name,
    yearValue,
    monthValue,
    dayValue,
    amount,
    bankRecordModels,
    fileAttachmentModels,
  )
}

function toFinancialTransactionModels(financialTransactions) {
  return financialTransactions.map(toFinancialTransactionModel)
}

function toDateAmountModel(dateAmount) {
  const { id, yearValue, monthValue, dayValue, amount } = dateAmount
  return new DateAmountModel(id, yearValue, monthValue, dayValue, amount)
}

function toDateAmountModels(dateAmounts) {
  return dateAmounts.map(toDateAmountModel)
}

function toFileAttachmentModel(fileAttachment) {
  const { id, name } = fileAttachment
  return new FileAttachmentModel(id, name)
}

function toFileAttachmentModels(fileAttachments) {
  return fileAttachments.map(toFileAttachmentModel)
}

// This function returns whether the user is authenticated
function verifyAuthenticated() {
  const authenticationStore = useAuthenticationStore()
  const isAuthenticated = authenticationStore.isAuthenticated
  if (!isAuthenticated) {
    console.error('Not authenticated')
    return false
  }
  return true
}

/*
 * These functions perform a request on the backend using the JSON token
 * They return a promise that resolves if the request was successful
 * And rejects if the request was unsuccessful or an error occurs
 * If verify is false, then don't check the response
 */

function authenticatedFnAsync(fn, relativeUrl, data, verify) {
  const authenticationStore = useAuthenticationStore()
  const jsonToken = authenticationStore.jsonToken
  const promise =
    data === null
      ? fn(baseUrl + relativeUrl, {
          headers: { Authorization: `Bearer ${jsonToken}` },
        })
      : fn(baseUrl + relativeUrl, data, {
          headers: { Authorization: `Bearer ${jsonToken}` },
        })
  if (verify) {
    return new Promise((resolve, reject) => {
      promise
        .then((response) => {
          if (response.data.success) {
            resolve(response.data.data)
          } else {
            const errorMessage = response.data.data
            reject(errorMessage)
          }
        })
        .catch((error) => reject(error))
    })
  } else {
    return promise
  }
}

function authenticatedGetAsync(relativeUrl) {
  return authenticatedFnAsync(axios.get, relativeUrl, null, true)
}

function authenticatedPostAsync(relativeUrl, data) {
  return authenticatedFnAsync(axios.post, relativeUrl, data, true)
}

function authenticatedPutAsync(relativeUrl, data) {
  return authenticatedFnAsync(axios.put, relativeUrl, data, true)
}

function authenticatedDeleteAsync(relativeUrl) {
  return authenticatedFnAsync(axios.delete, relativeUrl, null, false)
}

/*
 * These functions are helper functions to allow for loading and modifying data
 */

function loadDataFromMultipleSourcesAsync(sources) {
  return new Promise((resolve, reject) => {
    const promises = sources.map((source) => authenticatedGetAsync(source.relativeUrl))
    Promise.all(promises)
      .then((responses) => {
        const data = {}
        for (const i in sources) {
          const source = sources[i]
          const response = responses[i]
          const sourceName = source.name
          const sourceMapFunction = source.mapFunction
          data[sourceName] = sourceMapFunction(response)
        }
        resolve(data)
      })
      .catch((error) => {
        console.error(error)
        reject()
      })
  })
}

function loadFileByIdAsync(id) {
  const authenticationStore = useAuthenticationStore()
  const jsonToken = authenticationStore.jsonToken
  const promise = axios.get(`${baseUrl}/fileattachments/${id}`, {
    headers: { Authorization: `Bearer ${jsonToken}` },
    responseType: 'blob',
  })
  return new Promise((resolve, reject) => {
    promise
      .then((response) => {
        resolve(response)
      })
      .catch((error) => {
        console.error(error)
        reject(error)
      })
  })
}

function modifyDataAsync(relativeUrl, data, dataHandler, requestFunction) {
  return new Promise((resolve, reject) => {
    if (!verifyAuthenticated()) reject()
    const promise = data == null ? requestFunction(relativeUrl) : requestFunction(relativeUrl, data)
    promise
      .then(() => {
        dataHandler()
        resolve()
      })
      .catch((error) => {
        console.error(error)
        reject()
      })
  })
}

function attachFileAsync(file, itemId, dataHandler) {
  const authenticationStore = useAuthenticationStore()
  const jsonToken = authenticationStore.jsonToken
  return new Promise((resolve, reject) => {
    if (!verifyAuthenticated()) reject()
    const formData = new FormData()
    formData.append('file', file)
    axios
      .post(`${baseUrl}/fileattachments/${itemId}`, formData, {
        headers: { 'Content-Type': 'multipart/form-data', Authorization: `Bearer ${jsonToken}` },
      })
      .then(() => {
        dataHandler()
        resolve()
      })
      .catch((error) => {
        console.error(error)
        reject()
      })
  })
}

/*
 * This data store is responsible for:
 * - Retrieving data from the backend
 * - Creating, modifying, and deleting data on the backend
 * - Keeping track of whether the data loaded is what the page needs
 */
const useDataStore = defineStore('data', {
  state: () => {
    return {
      /** @type any */
      _data: null,
      /** @type any */
      _dataStatus: DataStatus.NOT_LOADED,
    }
  },

  getters: {
    data(state) {
      return state._data
    },
    dataStatus(state) {
      return state._dataStatus
    },
  },

  actions: {
    resetData() {
      console.log('Set data to NOT LOADED')
      this._dataStatus = DataStatus.NOT_LOADED
    },

    expireData() {
      console.log('Set data to EXPIRED')
      this._dataStatus = DataStatus.EXPIRED
    },

    loadDataAsync(sources) {
      return new Promise((resolve, reject) => {
        if (!verifyAuthenticated()) reject()
        const promise = loadDataFromMultipleSourcesAsync(sources)
        promise
          .then((data) => {
            this._data = data
            this._dataStatus = DataStatus.LOADED
            resolve()
          })
          .catch(() => {
            this._dataStatus = DataStatus.ERROR
            reject()
          })
        this._dataStatus = DataStatus.LOADING
      })
    },

    loadAccountsAsync() {
      return this.loadDataAsync([
        {
          name: 'accounts',
          relativeUrl: '/accounts',
          mapFunction: toAccountModels,
        },
      ])
    },

    loadSingleAccountAsync(id) {
      return this.loadDataAsync([
        {
          name: 'account',
          relativeUrl: `/accounts/${id}`,
          mapFunction: toAccountModel,
        },
      ])
    },

    loadBankRecordsAndDateAmountsAsync(accountId) {
      return this.loadDataAsync([
        {
          name: 'bankRecords',
          relativeUrl: `/bankrecords/${accountId}`,
          mapFunction: toBankRecordModels,
        },
        {
          name: 'dateAmounts',
          relativeUrl: `/dateamounts/${accountId}`,
          mapFunction: toDateAmountModels,
        },
      ])
    },

    loadBankRecordsAndDateAmountsDuringYearAsync(accountId, year) {
      return this.loadDataAsync([
        {
          name: `bankRecords`,
          relativeUrl: `/bankrecords/${accountId}?year=${year}`,
          mapFunction: toBankRecordModels,
        },
        {
          name: 'dateAmounts',
          relativeUrl: `/dateamounts/${accountId}?year=${year}`,
          mapFunction: toDateAmountModels,
        },
      ])
    },

    loadBankRecordsAndDateAmountsDuringMonthAsync(accountId, year, month) {
      return this.loadDataAsync([
        {
          name: `bankRecords`,
          relativeUrl: `/bankrecords/${accountId}?year=${year}&month=${month}`,
          mapFunction: toBankRecordModels,
        },
        {
          name: 'dateAmounts',
          relativeUrl: `/dateamounts/${accountId}?year=${year}&month=${month}`,
          mapFunction: toDateAmountModels,
        },
      ])
    },

    loadBankRecordsAndDateAmountsDuringDayAsync(accountId, year, month, day) {
      return this.loadDataAsync([
        {
          name: `bankRecords`,
          relativeUrl: `/bankrecords/${accountId}?year=${year}&month=${month}&day=${day}`,
          mapFunction: toBankRecordModels,
        },
        {
          name: 'dateAmounts',
          relativeUrl: `/dateamounts/${accountId}?year=${year}&month=${month}&day=${day}`,
          mapFunction: toDateAmountModels,
        },
      ])
    },

    loadSingleBankRecordAsync(accountId, id) {
      return this.loadDataAsync([
        {
          name: 'bankRecord',
          relativeUrl: `/bankrecords/${accountId}/${id}`,
          mapFunction: toBankRecordModelFull,
        },
      ])
    },

    loadFinancialTransactionsAsync(accountId) {
      return this.loadDataAsync([
        {
          name: 'financialTransactions',
          relativeUrl: `/financialtransactions/${accountId}`,
          mapFunction: toFinancialTransactionModels,
        },
      ])
    },

    loadFinancialTransactionsDuringYearAsync(accountId, year) {
      return this.loadDataAsync([
        {
          name: 'financialTransactions',
          relativeUrl: `/financialtransactions/${accountId}?year=${year}`,
          mapFunction: toFinancialTransactionModels,
        },
      ])
    },

    loadFinancialTransactionsDuringMonthAsync(accountId, year, month) {
      return this.loadDataAsync([
        {
          name: 'financialTransactions',
          relativeUrl: `/financialtransactions/${accountId}?year=${year}&month=${month}`,
          mapFunction: toFinancialTransactionModels,
        },
      ])
    },

    loadFinancialTransactionsDuringDayAsync(accountId, year, month, day) {
      return this.loadDataAsync([
        {
          name: 'financialTransactions',
          relativeUrl: `/financialtransactions/${accountId}?year=${year}&month=${month}&day=${day}`,
          mapFunction: toFinancialTransactionModels,
        },
      ])
    },

    loadSingleFinancialTransactionAsync(accountId, id) {
      return this.loadDataAsync([
        {
          name: 'financialTransaction',
          relativeUrl: `/financialtransactions/${accountId}/${id}`,
          mapFunction: toFinancialTransactionModelFull,
        },
      ])
    },

    loadBankRecordAndFinancialTransactionsAsync(accountId, id) {
      return this.loadDataAsync([
        {
          name: 'bankRecord',
          relativeUrl: `/bankrecords/${accountId}/${id}`,
          mapFunction: toBankRecordModelFull,
        },
        {
          name: 'financialTransactions',
          relativeUrl: `/financialtransactions/${accountId}`,
          mapFunction: toFinancialTransactionModels,
        },
      ])
    },

    loadBankRecordAndFinancialTransactionsDuringYearAsync(accountId, id, year) {
      return this.loadDataAsync([
        {
          name: 'bankRecord',
          relativeUrl: `/bankrecords/${accountId}/${id}`,
          mapFunction: toBankRecordModelFull,
        },
        {
          name: 'financialTransactions',
          relativeUrl: `/financialtransactions/${accountId}?year=${year}`,
          mapFunction: toFinancialTransactionModels,
        },
      ])
    },

    loadBankRecordAndFinancialTransactionsDuringMonthAsync(accountId, id, year, month) {
      return this.loadDataAsync([
        {
          name: 'bankRecord',
          relativeUrl: `/bankrecords/${accountId}/${id}`,
          mapFunction: toBankRecordModelFull,
        },
        {
          name: 'financialTransactions',
          relativeUrl: `/financialtransactions/${accountId}?year=${year}&month=${month}`,
          mapFunction: toFinancialTransactionModels,
        },
      ])
    },

    loadBankRecordAndFinancialTransactionsDuringDayAsync(accountId, id, year, month, day) {
      return this.loadDataAsync([
        {
          name: 'bankRecord',
          relativeUrl: `/bankrecords/${accountId}/${id}`,
          mapFunction: toBankRecordModelFull,
        },
        {
          name: 'financialTransactions',
          relativeUrl: `/financialtransactions/${accountId}?year=${year}&month=${month}&day=${day}`,
          mapFunction: toFinancialTransactionModels,
        },
      ])
    },

    loadFinancialTransactionAndBankRecordsAsync(accountId, id) {
      return this.loadDataAsync([
        {
          name: 'financialTransaction',
          relativeUrl: `/financialtransactions/${accountId}/${id}`,
          mapFunction: toFinancialTransactionModelFull,
        },
        {
          name: 'bankRecords',
          relativeUrl: `/bankRecords/${accountId}`,
          mapFunction: toBankRecordModels,
        },
      ])
    },

    loadFinancialTransactionAndBankRecordsDuringYearAsync(accountId, id, year) {
      return this.loadDataAsync([
        {
          name: 'financialTransaction',
          relativeUrl: `/financialtransactions/${accountId}/${id}`,
          mapFunction: toFinancialTransactionModelFull,
        },
        {
          name: 'bankRecords',
          relativeUrl: `/bankRecords/${accountId}?year=${year}`,
          mapFunction: toBankRecordModels,
        },
      ])
    },

    loadFinancialTransactionAndBankRecordsDuringMonthAsync(accountId, id, year, month) {
      return this.loadDataAsync([
        {
          name: 'financialTransaction',
          relativeUrl: `/financialtransactions/${accountId}/${id}`,
          mapFunction: toFinancialTransactionModelFull,
        },
        {
          name: 'bankRecords',
          relativeUrl: `/bankRecords/${accountId}?year=${year}&month=${month}`,
          mapFunction: toBankRecordModels,
        },
      ])
    },

    loadFinancialTransactionAndBankRecordsDuringDayAsync(accountId, id, year, month, day) {
      return this.loadDataAsync([
        {
          name: 'financialTransaction',
          relativeUrl: `/financialtransactions/${accountId}/${id}`,
          mapFunction: toFinancialTransactionModelFull,
        },
        {
          name: 'bankRecords',
          relativeUrl: `/bankRecords/${accountId}?year=${year}&month=${month}&day=${day}`,
          mapFunction: toBankRecordModels,
        },
      ])
    },

    loadFileAttachmentAsync(id) {
      return loadFileByIdAsync(id)
    },

    createAccountAsync(account) {
      console.log('Create Account', { account })
      return modifyDataAsync(`/accounts`, account, this.expireData, authenticatedPostAsync)
    },

    createBankRecordAsync(accountId, bankRecord) {
      console.log('Create Bank Record', { bankRecord })
      return modifyDataAsync(
        `/bankrecords/${accountId}`,
        bankRecord,
        this.expireData,
        authenticatedPostAsync,
      )
    },

    createDateAmountAsync(accountId, dateAmount) {
      console.log('Create Date Amount', { dateAmount })
      return modifyDataAsync(
        `/dateamount/${accountId}`,
        dateAmount,
        this.expireData,
        authenticatedPostAsync,
      )
    },

    createFinancialTransactionAsync(accountId, financialTransaction) {
      console.log('Create Financial Transaction', { financialTransaction })
      return modifyDataAsync(
        `/financialtransactions/${accountId}`,
        financialTransaction,
        this.expireData,
        authenticatedPostAsync,
      )
    },

    attachFileToItemAsync(file, itemId) {
      console.log('Attaching File', { itemId })
      return attachFileAsync(file, itemId, this.expireData)
    },

    updateAccountAsync(id, account) {
      console.log('Update Account', { id, account })
      return modifyDataAsync(`/accounts/${id}`, account, this.expireData, authenticatedPutAsync)
    },

    updateBankRecordAsync(accountId, id, bankRecord) {
      console.log('Update Bank Record', { id, bankRecord })
      return modifyDataAsync(
        `/bankrecords/${accountId}/${id}`,
        bankRecord,
        this.expireData,
        authenticatedPutAsync,
      )
    },

    updateDateAmountAsync(accountId, yearValue, monthValue, dayValue, dateAmount) {
      console.log('Update Date Amount', { yearValue, monthValue, dayValue, dateAmount })
      return modifyDataAsync(
        `/dateamounts/${accountId}?year=${yearValue}&month=${monthValue}&day=${dayValue}`,
        { amount: dateAmount },
        this.expireData,
        authenticatedPutAsync,
      )
    },

    updateFinancialTransactionAsync(accountId, id, financialTransaction) {
      console.log('Update Financial Transaction', { id, financialTransaction })
      return modifyDataAsync(
        `/financialtransactions/${accountId}/${id}`,
        financialTransaction,
        this.expireData,
        authenticatedPutAsync,
      )
    },

    deleteAccountAsync(id) {
      console.log('Delete Account', { id })
      return modifyDataAsync(`/accounts/${id}`, null, this.expireData, authenticatedDeleteAsync)
    },

    deleteBankRecordAsync(accountId, id) {
      console.log('Delete Bank Record', { id })
      return modifyDataAsync(
        `/bankrecords/${accountId}/${id}`,
        null,
        this.expireData,
        authenticatedDeleteAsync,
      )
    },

    deleteDateAmountAsync(accountId, yearValue, monthValue, dayValue) {
      console.log('Delete Date Amount', { yearValue, monthValue, dayValue })
      return modifyDataAsync(
        `/dateamounts/${accountId}?year=${yearValue}&month=${monthValue}&day=${dayValue}`,
        null,
        this.expireData,
        authenticatedDeleteAsync,
      )
    },

    deleteFinancialTransactionAsync(accountId, id) {
      console.log('Delete Financial Transaction', { id })
      return modifyDataAsync(
        `/financialtransactions/${accountId}/${id}`,
        null,
        this.expireData,
        authenticatedDeleteAsync,
      )
    },

    deleteFileAttachmentAsync(id) {
      console.log('Delete File Attachment', { id })
      return modifyDataAsync(
        `/fileattachments/${id}`,
        null,
        this.expireData,
        authenticatedDeleteAsync,
      )
    },

    attachRecordAndTransactionAsync(recordId, transactionId) {
      console.log('Attach Record and Transaction', { recordId, transactionId })
      return modifyDataAsync(
        `/recordtransactions/${recordId}/${transactionId}`,
        null,
        this.expireData,
        authenticatedPostAsync,
      )
    },

    detachRecordAndTransactionAsync(recordId, transactionId) {
      console.log('Detach Record and Transaction', { recordId, transactionId })
      return modifyDataAsync(
        `/recordtransactions/${recordId}/${transactionId}`,
        null,
        this.expireData,
        authenticatedDeleteAsync,
      )
    },
  },
})

export default useDataStore
