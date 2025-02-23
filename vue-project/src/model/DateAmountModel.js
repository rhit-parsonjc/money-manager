import { DateObjectModel } from './DateObjectModel'

class DateAmountModel {
  constructor(id, yearValue, monthValue, dayValue, amount) {
    this.id = id
    this.dateObj = new DateObjectModel(yearValue, monthValue, dayValue)
    this.amount = amount
  }
}

function sortAmountModels(amountOne, amountTwo) {
  return amountOne.dateObj.compareTo(amountTwo.dateObj)
}

export { sortAmountModels, DateAmountModel }
