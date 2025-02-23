function formatCurrency(value) {
  if (value > 0) {
    return `+$${value.toFixed(2)}`
  } else if (value < 0) {
    return `-$${(-value).toFixed(2)}`
  } else {
    return '$0.00'
  }
}

export { formatCurrency }
