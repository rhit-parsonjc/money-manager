import { DateObjectModel } from './DateObjectModel'

class BankRecordModel {
  constructor(name, yearValue, monthValue, dayValue, amount) {
    this.name = name
    this.dateObj = new DateObjectModel(yearValue, monthValue, dayValue)
    this.amount = amount
  }
}

function sortRecordModels(recordOne, recordTwo) {
  return recordOne.dateObj.dateValue - recordTwo.dateObj.dateValue
}

export { BankRecordModel, sortRecordModels }
