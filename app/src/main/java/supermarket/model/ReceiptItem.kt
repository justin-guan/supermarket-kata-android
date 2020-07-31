package supermarket.model

import currency.model.Currency

data class ReceiptItem(val product: Product, val quantity: Double, val price: Currency, val totalPrice: Currency)
