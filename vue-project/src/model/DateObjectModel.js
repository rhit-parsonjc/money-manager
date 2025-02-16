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

// const dayNames = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']

// Month ranges from 1 to 12
// Day ranges from 1 to the maximum number of days in the month

class DateObjectModel {
  constructor(yearValue, monthValue, dayValue) {
    if (isValid(yearValue, monthValue, dayValue)) {
      this.yearValue = yearValue
      this.monthValue = monthValue
      this.dayValue = dayValue
    } else {
      console.error('Invalid date', { yearValue, monthValue, dayValue })
    }
  }

  format() {
    return `${monthNames[this.monthValue - 1]} ${this.dayValue}, ${this.yearValue}`
  }

  equals(dateObj) {
    return (
      this.yearValue === dateObj.yearValue &&
      this.monthValue === dateObj.monthValue &&
      this.dayValue === dateObj.dayValue
    )
  }

  after(dateObj) {
    if (this.yearValue > dateObj.yearValue) return true
    if (this.yearValue < dateObj.yearValue) return false
    if (this.monthValue > dateObj.monthValue) return true
    if (this.monthValue < dateObj.monthValue) return false
    return this.dayValue > dateObj.dayValue
  }

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

  clone() {
    return new DateObjectModel(this.yearValue, this.monthValue, this.dayValue)
  }
}

function isValid(yearValue, monthValue, dayValue) {
  if (monthValue < 1 || monthValue > 12) return false
  if (dayValue < 1 || dayValue > daysPerMonth(monthNames[monthValue - 1], yearValue)) return false
  return true
}

function monthNameFromNumber(monthValue) {
  return monthNames[monthValue - 1]
}

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

function getFirstDayOfYear(yearValue) {
  return new DateObjectModel(yearValue, 1, 1)
}

function getLastDayOfYear(yearValue) {
  return new DateObjectModel(yearValue, 12, 31)
}

function getFirstDayOfMonth(yearValue, monthValue) {
  return new DateObjectModel(yearValue, monthValue, 1)
}

function getLastDayOfMonth(yearValue, monthValue) {
  return new DateObjectModel(
    yearValue,
    monthValue,
    daysPerMonth(monthNames[monthValue - 1], yearValue),
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
