/*
 * A BankRecordModel consists of
 * - id (number)
 * - name (string)
 * - dateObj (DateObjectModel)
 * - amount (number)
 * - financialTransactions (FinancialTransactionModel list, null if nested)
 * - fileAttachments (FileAttachmentModel list, null if nested)
 *
 * This represents information for a single bank record.
 * The financial transactions and file attachments are sorted
 * In the constructor.
 */

import { DateObjectModel } from './DateObjectModel'
import { sortFileAttachmentModels } from './FileAttachmentModel'
import { sortTransactionModels } from './FinancialTransactionModel'

class BankRecordModel {
  constructor(
    id,
    name,
    yearValue,
    monthValue,
    dayValue,
    amount,
    financialTransactions,
    fileAttachments,
  ) {
    this.id = id
    this.name = name
    this.dateObj = new DateObjectModel(yearValue, monthValue, dayValue)
    this.amount = amount
    this.financialTransactions =
      financialTransactions === null
        ? financialTransactions
        : [...financialTransactions].sort(sortTransactionModels)
    this.fileAttachments =
      fileAttachments === null
        ? fileAttachments
        : [...fileAttachments].sort(sortFileAttachmentModels)
  }
}

function sortRecordModels(recordOne, recordTwo) {
  return recordOne.dateObj.compareTo(recordTwo.dateObj)
}

export { BankRecordModel, sortRecordModels }
