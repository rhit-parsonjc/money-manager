/*
 * A DateAndRecordsModel consists of
 * - dateObj (DateObjectModel)
 * - financialTransactions (FinancialTransactionModel list)
 *
 * This represents a date, with associated financial transactions.
 */

import { sortTransactionModels } from './FinancialTransactionModel'

class DateAndTransactionsModel {
  constructor(dateObj, financialTransactions) {
    this.dateObj = dateObj
    this.financialTransactions = financialTransactions
  }
}

function organizeTransactionsByDate(transactions, firstDateObj = null, lastDateObj = null) {
  // Sort the transactions
  const sortedTransactions = [...transactions].sort(sortTransactionModels)
  /*
    If a first and last date are provided, use these.
    Otherwise, use the earliest date in the transactions
    And the latest date in the transactions
  */
  const transactionsPerDate = []
  if (firstDateObj == null) {
    if (sortedTransactions.length > 0) {
      firstDateObj = sortedTransactions[0].dateObj
      lastDateObj = sortedTransactions[sortedTransactions.length - 1].dateObj
    } else {
      return transactionsPerDate
    }
  }
  // Go through all of the days...
  let transactionIndex = 0
  for (
    const dateObj = firstDateObj.clone();
    dateObj.compareTo(lastDateObj) <= 0;
    dateObj.increment()
  ) {
    // Find all of the transactions on this day
    const transactionList = []
    while (
      transactionIndex < sortedTransactions.length &&
      dateObj.equals(sortedTransactions[transactionIndex].dateObj)
    ) {
      transactionList.push(sortedTransactions[transactionIndex])
      transactionIndex++
    }
    // Create a new DateAndTransactionsModel.
    transactionsPerDate.push(new DateAndTransactionsModel(dateObj.clone(), transactionList))
  }
  return transactionsPerDate
}

export { DateAndTransactionsModel, organizeTransactionsByDate }
