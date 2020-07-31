package supermarket.model

import currency.model.Currency

class Offer(private val offerType: SpecialOfferType, private val product: Product) {
    fun getDiscount(purchaseQuantity: Int, unitPrice: Currency) =
        offerType.calculateDiscount(product, purchaseQuantity, unitPrice)
}
