function formatCurrency(value) {
  const signString = value === 0 ? '' : value > 0 ? '+' : '-'
  if (value < 0) value = -value
  const cents = value % 100
  const centsOnesDigit = cents % 10
  const centsTensDigit = (cents - centsOnesDigit) / 10
  const dollars = (value - cents) / 100
  return `${signString}$${dollars}.${centsTensDigit}${centsOnesDigit}`
}

function getDollarsValue(amountValue) {
  if (amountValue < 0) {
    return -getDollarsValue(-amountValue)
  } else {
    return (amountValue - (amountValue % 100)) / 100
  }
}

function getCentsValue(amountValue) {
  if (amountValue < 0) {
    return getCentsValue(-amountValue)
  } else {
    return amountValue % 100
  }
}

function getAmountValue(dollarsValue, centsValue) {
  if (dollarsValue < 0) {
    return dollarsValue * 100 - centsValue
  } else {
    return dollarsValue * 100 + centsValue
  }
}

export { formatCurrency, getDollarsValue, getCentsValue, getAmountValue }
