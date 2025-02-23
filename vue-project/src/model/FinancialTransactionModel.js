import { DateObjectModel } from './DateObjectModel'

class FinancialTransactionModel {
  constructor(id, name, yearValue, monthValue, dayValue, amount) {
    this.id = id
    this.name = name
    this.dateObj = new DateObjectModel(yearValue, monthValue, dayValue)
    this.amount = amount
  }
}

function sortTransactionModels(transactionOne, transactionTwo) {
  return transactionOne.dateObj.compareTo(transactionTwo.dateObj)
}

export { FinancialTransactionModel, sortTransactionModels }
