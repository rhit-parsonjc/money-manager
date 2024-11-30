import { sortRecordModels } from './BankRecordModel'

class DateRecordModel {
  constructor(dateObj, records) {
    this.dateObj = dateObj
    this.records = records
  }
}

function organizeRecordsByDate(records) {
  const sortedRecords = [...records].sort(sortRecordModels)
  const firstDateObj = sortedRecords[0].dateObj
  const lastDateObj = sortedRecords[sortedRecords.length - 1].dateObj
  const recordsPerDate = []
  let recordIndex = 0
  for (const dateObj = firstDateObj.clone(); !dateObj.after(lastDateObj); dateObj.increment()) {
    const recordList = []
    while (
      recordIndex < sortedRecords.length &&
      dateObj.equals(sortedRecords[recordIndex].dateObj)
    ) {
      recordList.push(sortedRecords[recordIndex])
      recordIndex++
    }
    recordsPerDate.push(new DateRecordModel(dateObj.clone(), recordList))
  }
  return recordsPerDate
}

export { DateRecordModel, organizeRecordsByDate }
