package supermarket.model

class Offer(private val offerType: SpecialOfferType, internal val product: Product) {
    fun getDiscount(purchaseQuantity: Int, unitPrice: Double) =
        offerType.calculateDiscount(product, purchaseQuantity, unitPrice)
}
