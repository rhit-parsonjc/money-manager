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
 * The bank records and file attachments are sorted in the constructor.
 */

import { sortRecordModels } from './BankRecordModel'
import { DateObjectModel } from './DateObjectModel'
import { sortFileAttachmentModels } from './FileAttachmentModel'

class FinancialTransactionModel {
  constructor(id, name, yearValue, monthValue, dayValue, amount, bankRecords, fileAttachments) {
    this.id = id
    this.name = name
    this.dateObj = new DateObjectModel(yearValue, monthValue, dayValue)
    this.amount = amount
    this.bankRecords = bankRecords === null ? bankRecords : [...bankRecords].sort(sortRecordModels)
    this.fileAttachments =
      fileAttachments === null
        ? fileAttachments
        : [...fileAttachments].sort(sortFileAttachmentModels)
  }
}

function sortTransactionModels(transactionOne, transactionTwo) {
  return transactionOne.dateObj.compareTo(transactionTwo.dateObj)
}

export { FinancialTransactionModel, sortTransactionModels }
