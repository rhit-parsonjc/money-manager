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
    this.dateValue = new Date()
    this.dateValue.setUTCFullYear(yearValue, monthValue - 1, dayValue)
    this.dateValue.setUTCHours(0, 0, 0, 0)
  }

  get yearValue() {
    return this.dateValue.getUTCFullYear()
  }

  get monthValue() {
    return this.dateValue.getUTCMonth() + 1
  }

  get dayValue() {
    return this.dateValue.getUTCDate()
  }

  get dayOfWeekValue() {
    return this.dateValue.getUTCDay()
  }

  format() {
    return `${monthNames[this.monthValue - 1]} ${this.dayValue}, ${this.yearValue}`
  }

  equals(dateObj) {
    return this.dateValue.getTime() === dateObj.dateValue.getTime()
  }

  after(dateObj) {
    return this.dateValue > dateObj.dateValue
  }

  increment() {
    this.dateValue.setDate(this.dateValue.getDate() + 1)
  }

  clone() {
    return new DateObjectModel(this.yearValue, this.monthValue, this.dayValue)
  }
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
    May: 30,
    June: 31,
    July: 31,
    August: 31,
    September: 30,
    October: 31,
    November: 30,
    December: 31,
  }
  return daysPerMonth[monthName]
}

export { monthNames, DateObjectModel, monthNameFromNumber, daysPerMonth }
