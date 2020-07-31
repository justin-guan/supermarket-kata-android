package supermarket.model

import currency.model.Currency

data class Discount(val product: Product, val description: String, val discountAmount: Currency)
