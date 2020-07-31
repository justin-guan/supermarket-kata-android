package supermarket.model

import currency.model.Currency
import currency.model.div
import currency.model.minus
import currency.model.plus
import currency.model.times

sealed class SpecialOfferType {

    abstract fun calculateDiscount(product: Product, purchaseQuantity: Int, unitPrice: Currency): Discount?

    data class QuantityForAmount(val requiredQuantity: Double, val amount: Currency) : SpecialOfferType() {
        override fun calculateDiscount(
            product: Product,
            purchaseQuantity: Int,
            unitPrice: Currency
        ): Discount? {
            val discountAmount = if (purchaseQuantity >= requiredQuantity) {
                val totalAfterDiscount =
                    amount * (purchaseQuantity / requiredQuantity).toInt() + purchaseQuantity % requiredQuantity * unitPrice
                val totalWithoutDiscount = purchaseQuantity * unitPrice
                totalWithoutDiscount - totalAfterDiscount
            } else {
                return null
            }
            val description = "$requiredQuantity for $amount"
            return Discount(product, description, discountAmount)
        }
    }

    data class PercentageDiscount(val discountPercentage: Double) : SpecialOfferType() {
        override fun calculateDiscount(
            product: Product,
            purchaseQuantity: Int,
            unitPrice: Currency
        ): Discount? {
            val discountAmount = purchaseQuantity * unitPrice * discountPercentage / 100.0
            return Discount(product, "$discountPercentage% off", discountAmount)
        }
    }
}
