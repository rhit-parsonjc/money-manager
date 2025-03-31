/*
 * A DateAndRecordsModel consists of
 * - dateObj (DateObjectModel)
 * - bankRecords (BankRecordModel list)
 * - amount (number or null)
 *
 * This represents a date, with associated amounts and bankRecords.
 */

import { sortRecordModels } from './BankRecordModel'
import { sortAmountModels } from './DateAmountModel'

class DateAndRecordsModel {
  constructor(dateObj, bankRecords, amount) {
    this.dateObj = dateObj
    this.bankRecords = bankRecords
    this.amount = amount
  }
}

function organizeRecordsByDate(records, amounts, firstDateObj = null, lastDateObj = null) {
  // Sort the amounts and records
  const sortedAmounts = [...amounts].sort(sortAmountModels)
  const sortedRecords = [...records].sort(sortRecordModels)
  /*
    If a first and last date are provided, use these.
    Otherwise, use the earliest date in the amounts and records
    And the latest date in the amounts and records
  */
  const recordsPerDate = []
  if (firstDateObj == null) {
    if (sortedAmounts.length > 0) {
      firstDateObj = sortedAmounts[0].dateObj
      lastDateObj = sortedAmounts[sortedAmounts.length - 1].dateObj
    }
    if (sortedRecords.length > 0) {
      const firstRecordDateObj = sortedRecords[0].dateObj
      const lastRecordDateObj = sortedRecords[sortedRecords.length - 1].dateObj
      if (firstDateObj === null || firstRecordDateObj.compareTo(firstDateObj) < 0)
        firstDateObj = firstRecordDateObj
      if (lastDateObj === null || lastRecordDateObj.compareTo(lastDateObj) > 0)
        lastDateObj = lastRecordDateObj
    }
    if (firstDateObj === null) return recordsPerDate
  }
  // Go through all of the days...
  let recordIndex = 0
  let amountIndex = 0
  for (
    const dateObj = firstDateObj.clone();
    dateObj.compareTo(lastDateObj) <= 0;
    dateObj.increment()
  ) {
    // Find all of the records that are on this date.
    const recordList = []
    while (
      recordIndex < sortedRecords.length &&
      dateObj.equals(sortedRecords[recordIndex].dateObj)
    ) {
      recordList.push(sortedRecords[recordIndex])
      recordIndex++
    }
    // If there is an amount with this date, record it.
    let amount = null
    if (amountIndex < sortedAmounts.length && dateObj.equals(sortedAmounts[amountIndex].dateObj)) {
      amount = sortedAmounts[amountIndex].amount
      amountIndex++
    }
    // Create a new DateAndRecords object.
    recordsPerDate.push(new DateAndRecordsModel(dateObj.clone(), recordList, amount))
  }
  return recordsPerDate
}

export { DateAndRecordsModel, organizeRecordsByDate }
