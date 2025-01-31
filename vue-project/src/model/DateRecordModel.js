import { sortRecordModels } from './BankRecordModel'
import { sortAmountModels } from './DateAmountModel'

class DateRecordModel {
  constructor(dateObj, records, amount) {
    this.dateObj = dateObj
    this.records = records
    this.amount = amount
  }
}

function organizeRecordsByDate(records, amounts) {
  const sortedAmounts = [...amounts].sort(sortAmountModels)
  const sortedRecords = [...records].sort(sortRecordModels)
  let firstDateObj = null
  let lastDateObj = null
  if (sortedAmounts.length > 0) {
    firstDateObj = sortedAmounts[0].dateObj
    lastDateObj = sortedAmounts[sortedAmounts.length - 1].dateObj
  }
  if (sortedRecords.length > 0) {
    const firstRecordDateObj = sortedRecords[0].dateObj
    const lastRecordDateObj = sortedRecords[sortedRecords.length - 1].dateObj
    if (firstDateObj === null || firstRecordDateObj.dateValue < firstDateObj.dateValue)
      firstDateObj = firstRecordDateObj
    if (lastDateObj === null || lastRecordDateObj.dateValue > lastDateObj.dateValue)
      lastDateObj = lastRecordDateObj
  }
  const recordsPerDate = []
  if (firstDateObj === null) return recordsPerDate
  let recordIndex = 0
  let amountIndex = 0
  for (const dateObj = firstDateObj.clone(); !dateObj.after(lastDateObj); dateObj.increment()) {
    const recordList = []
    while (
      recordIndex < sortedRecords.length &&
      dateObj.equals(sortedRecords[recordIndex].dateObj)
    ) {
      recordList.push(sortedRecords[recordIndex])
      recordIndex++
    }
    let amount = null
    if (amountIndex < sortedAmounts.length && dateObj.equals(sortedAmounts[amountIndex].dateObj)) {
      amount = sortedAmounts[amountIndex].amount
      amountIndex++
    }
    recordsPerDate.push(new DateRecordModel(dateObj.clone(), recordList, amount))
  }
  return recordsPerDate
}

export { DateRecordModel, organizeRecordsByDate }
