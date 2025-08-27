function formatCurrency(value) {
  const signString = value === 0 ? '' : value > 0 ? '+' : '-'
  if (value < 0) value = -value
  const cents = value % 100
  const centsOnesDigit = cents % 10
  const centsTensDigit = (cents - centsOnesDigit) / 10
  const dollars = (value - cents) / 100
  return `${signString}$${dollars}.${centsTensDigit}${centsOnesDigit}`
}

export { formatCurrency }
