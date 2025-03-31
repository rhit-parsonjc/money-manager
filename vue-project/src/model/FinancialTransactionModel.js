/*
 * A FinancialTransactionModel consists of
 * - id (number)
 * - name (string)
 * - dateObj (DateObjectModel)
 * - amount (number)
 * - bankRecords (BankRecordModel list, null if nested)
 * - fileAttachments (FileAttachmentModel list, null if nested)
 *
 * This represents information for a single financial transaction.
 */

import { DateObjectModel } from './DateObjectModel'

class FinancialTransactionModel {
  constructor(id, name, yearValue, monthValue, dayValue, amount, bankRecords, fileAttachments) {
    this.id = id
    this.name = name
    this.dateObj = new DateObjectModel(yearValue, monthValue, dayValue)
    this.amount = amount
    this.bankRecords = bankRecords
    this.fileAttachments = fileAttachments
  }
}

function sortTransactionModels(transactionOne, transactionTwo) {
  return transactionOne.dateObj.compareTo(transactionTwo.dateObj)
}

export { FinancialTransactionModel, sortTransactionModels }
