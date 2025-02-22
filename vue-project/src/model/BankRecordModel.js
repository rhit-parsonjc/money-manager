/*
 * A BankRecordModel contains an id, name, dateObj, and amount
 */

import { DateObjectModel } from './DateObjectModel'

class BankRecordModel {
  constructor(id, name, yearValue, monthValue, dayValue, amount) {
    this.id = id
    this.name = name
    this.dateObj = new DateObjectModel(yearValue, monthValue, dayValue)
    this.amount = amount
  }
}

function sortRecordModels(recordOne, recordTwo) {
  return recordOne.dateObj.compareTo(recordTwo.dateObj)
}

export { BankRecordModel, sortRecordModels }
