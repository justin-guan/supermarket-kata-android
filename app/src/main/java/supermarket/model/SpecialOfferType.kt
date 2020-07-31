package supermarket.model

sealed class SpecialOfferType {

    abstract fun calculateDiscount(product: Product, purchaseQuantity: Int, unitPrice: Double): Discount?

    data class QuantityForAmount(val requiredQuantity: Double, val amount: Double) : SpecialOfferType() {
        override fun calculateDiscount(
            product: Product,
            purchaseQuantity: Int,
            unitPrice: Double
        ): Discount? {
            val discountAmount = if (purchaseQuantity >= requiredQuantity) {
                val totalAfterDiscount =
                    amount * (purchaseQuantity / requiredQuantity).toInt() + purchaseQuantity % requiredQuantity * unitPrice
                val totalWithoutDiscount = purchaseQuantity * unitPrice
                totalWithoutDiscount - totalAfterDiscount
            } else {
                return null
            }
            return Discount(product, "$requiredQuantity for $amount", discountAmount)
        }
    }

    data class PercentageDiscount(val discountPercentage: Double) : SpecialOfferType() {
        override fun calculateDiscount(
            product: Product,
            purchaseQuantity: Int,
            unitPrice: Double
        ): Discount? {
            val discountAmount = purchaseQuantity * unitPrice * discountPercentage / 100.0
            return Discount(product, "$discountPercentage% off", discountAmount)
        }
    }
}
