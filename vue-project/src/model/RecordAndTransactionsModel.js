/*
 * A RecordAndTransactionsModel consists of
 * - bankRecord (BankRecordModel)
 * - financialTransactions (FinancialTransactionModel list)
 * - otherFinancialTransactions (FinancialTransactionModel list)
 *
 * This represents a bank record and separates all loaded financial
 * transactions based on whether they are attached to this bank
 * record (financialTransactions) or not (otherFinancialTransactions)
 */

import { sortTransactionModels } from './FinancialTransactionModel'

class RecordAndTransactionsModel {
  constructor(bankRecord, financialTransactions, allFinancialTransactions) {
    this.bankRecord = bankRecord
    // Map the ID to a boolean indicating whether this financial transaction
    // Is associated with the specified bank record
    const isAttached = {}
    for (const financialTransaction of allFinancialTransactions) {
      const id = financialTransaction.id
      isAttached[id] = false
    }
    for (const financialTransaction of financialTransactions) {
      const id = financialTransaction.id
      isAttached[id] = true
    }
    // Determine which financial transactions are not associated with
    // The bank record
    const otherFinancialTransactions = []
    for (const financialTransaction of allFinancialTransactions) {
      const id = financialTransaction.id
      if (!isAttached[id]) otherFinancialTransactions.push(financialTransaction)
    }
    const sortedFinancialTransactions = [...financialTransactions].sort(sortTransactionModels)
    const sortedOtherFinancialTransactions = [...otherFinancialTransactions].sort(
      sortTransactionModels,
    )
    this.financialTransactions = sortedFinancialTransactions
    this.otherFinancialTransactions = sortedOtherFinancialTransactions
  }
}

export { RecordAndTransactionsModel }
