package supermarket.model

class Receipt {
    private val items = mutableListOf<ReceiptItem>()
    private val discounts = mutableListOf<Discount>()

    val totalPrice: Double
        get() {
            var total = 0.0
            for (item in this.items) {
                total += item.totalPrice
            }
            for (discount in this.discounts) {
                total -= discount.discountAmount
            }
            return total
        }

    fun addProduct(p: Product, quantity: Double, price: Double, totalPrice: Double) {
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
