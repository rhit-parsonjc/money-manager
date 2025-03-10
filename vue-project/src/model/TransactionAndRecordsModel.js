import { sortRecordModels } from './BankRecordModel'

class TransactionAndRecordsModel {
  constructor(financialTransaction, bankRecords, allBankRecords) {
    this.financialTransaction = financialTransaction
    // Map the ID to a boolean indicating whether this bank record
    // Is associated with the specified financial transaction
    const isAttached = {}
    for (const bankRecord of allBankRecords) {
      const id = bankRecord.id
      isAttached[id] = false
    }
    for (const bankRecord of bankRecords) {
      const id = bankRecord.id
      isAttached[id] = true
    }
    // Determine which bank records are not associated with
    // The financial transaction
    const otherBankRecords = []
    for (const bankRecord of allBankRecords) {
      const id = bankRecord.id
      if (!isAttached[id]) otherBankRecords.push(bankRecord)
    }
    const sortedBankRecords = [...bankRecords].sort(sortRecordModels)
    const sortedOtherBankRecords = [...otherBankRecords].sort(sortRecordModels)
    this.bankRecords = sortedBankRecords
    this.otherBankRecords = sortedOtherBankRecords
  }
}

export { TransactionAndRecordsModel }
