/*
 * A DateObjectModel represents a day of the year
 */

const monthNames = [
  'January',
  'February',
  'March',
  'April',
  'May',
  'June',
  'July',
  'August',
  'September',
  'October',
  'November',
  'December',
]

class DateObjectModel {
  // Constructs a DateObjectModel with the specified year, month, and day
  constructor(yearValue, monthValue, dayValue) {
    if (isValid(yearValue, monthValue, dayValue)) {
      this.yearValue = yearValue
      this.monthValue = monthValue
      this.dayValue = dayValue
    } else {
      console.error('Invalid date', { yearValue, monthValue, dayValue })
    }
  }

  // Formats a date object in Month Day, Year format
  format() {
    return `${monthNameFromNumber(this.monthValue)} ${this.dayValue}, ${this.yearValue}`
  }

  // Returns whether this equals the other date object
  equals(dateObj) {
    return (
      this.yearValue === dateObj.yearValue &&
      this.monthValue === dateObj.monthValue &&
      this.dayValue === dateObj.dayValue
    )
  }

  // Converts to YYYYMMDD format
  toNumber() {
    return this.yearValue * 10000 + this.monthValue * 100 + this.dayValue
  }

  // Returns a positive number if this comes after dateObj
  // Returns zero if this equals dateObj
  // Returns a negative number if this comes before dateObj
  compareTo(dateObj) {
    return this.toNumber() - dateObj.toNumber()
  }

  // Increments this to the next day
  increment() {
    this.dayValue++
    if (this.dayValue > daysPerMonth(monthNames[this.monthValue - 1], this.yearValue)) {
      this.dayValue = 1
      this.monthValue++
      if (this.monthValue > 12) {
        this.monthValue = 1
        this.yearValue++
      }
    }
  }

  // Creates a clone of this DateObjectModel
  clone() {
    return new DateObjectModel(this.yearValue, this.monthValue, this.dayValue)
  }
}

// Returns whether this is a valid date
function isValid(yearValue, monthValue, dayValue) {
  if (monthValue < 1 || monthValue > 12) return false
  if (dayValue < 1 || dayValue > daysPerMonth(monthNames[monthValue - 1], yearValue)) return false
  return true
}

// Maps a month value (1-12) to the month of the year
function monthNameFromNumber(monthValue) {
  return monthNames[monthValue - 1]
}

// Returns the number of days given the month name and year
function daysPerMonth(monthName, yearValue) {
  const isLeapYear = yearValue % 400 === 0 || (yearValue % 100 !== 0 && yearValue % 4 === 0)
  const daysPerMonth = {
    January: 31,
    February: isLeapYear ? 29 : 28,
    March: 31,
    April: 30,
    May: 31,
    June: 30,
    July: 31,
    August: 31,
    September: 30,
    October: 31,
    November: 30,
    December: 31,
  }
  return daysPerMonth[monthName]
}

// Returns a DateObjectModel representing the first day of the year
function getFirstDayOfYear(yearValue) {
  return new DateObjectModel(yearValue, 1, 1)
}

// Returns a DateObjectModel representing the last day of the year
function getLastDayOfYear(yearValue) {
  return new DateObjectModel(yearValue, 12, 31)
}

// Returns a DateObjectModel representing the first day of the month
function getFirstDayOfMonth(yearValue, monthValue) {
  return new DateObjectModel(yearValue, monthValue, 1)
}

// Returns a DateObejctModel representing the last day of the month
function getLastDayOfMonth(yearValue, monthValue) {
  return new DateObjectModel(
    yearValue,
    monthValue,
    daysPerMonth(monthNameFromNumber(monthValue), yearValue),
  )
}

export {
  monthNames,
  DateObjectModel,
  monthNameFromNumber,
  daysPerMonth,
  getFirstDayOfYear,
  getLastDayOfYear,
  getFirstDayOfMonth,
  getLastDayOfMonth,
}
