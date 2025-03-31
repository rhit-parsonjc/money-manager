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
 */

import { DateObjectModel } from './DateObjectModel'

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
    this.financialTransactions = financialTransactions
    this.fileAttachments = fileAttachments
  }
}

function sortRecordModels(recordOne, recordTwo) {
  return recordOne.dateObj.compareTo(recordTwo.dateObj)
}

export { BankRecordModel, sortRecordModels }
