package supermarket.model

import currency.model.Currency
import currency.model.and
import currency.model.cents
import currency.model.dollars
import currency.model.minus
import currency.model.plus

class Receipt {
    private val items = mutableListOf<ReceiptItem>()
    private val discounts = mutableListOf<Discount>()

    val totalPrice: Currency
        get() {
            var total = 0.dollars and 0.cents
            for (item in this.items) {
                total += item.totalPrice
            }
            for (discount in this.discounts) {
                total -= discount.discountAmount
            }
            return total
        }

    fun addProduct(p: Product, quantity: Double, price: Currency, totalPrice: Currency) {
        this.items.add(ReceiptItem(p, quantity, price, totalPrice))
    }

    fun getItems(): List<ReceiptItem> {
        return items
    }

    fun addDiscount(discount: Discount) {
        this.discounts.add(discount)
    }

    fun getDiscounts(): List<Discount> {
        return discounts
    }
}
