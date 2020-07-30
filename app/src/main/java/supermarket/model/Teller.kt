package supermarket.model

import currency.model.times

class Teller(private val catalog: SupermarketCatalog) {
    private val offers = mutableMapOf<Product, Offer>()

    fun addSpecialOffer(offerType: SpecialOfferType, product: Product) {
        offers[product] = Offer(offerType, product)
    }

    fun checksOutArticlesFrom(theCart: ShoppingCart): Receipt {
        return Receipt().apply {
            addProducts(theCart, catalog)
            addDiscountsFromOffers(theCart, offers, catalog)
        }
    }
}

private fun Receipt.addProducts(cart: ShoppingCart, catalog: SupermarketCatalog) {
    val productQuantities = cart.getItems()
    for ((product, quantity) in productQuantities) {
        val unitPrice = catalog.getUnitPrice(product)
        val price = quantity * unitPrice
        addProduct(product, quantity, unitPrice, price)
    }
}

private fun Receipt.addDiscountsFromOffers(cart: ShoppingCart, offers: Map<Product, Offer>, catalog: SupermarketCatalog) {
    cart.getItems().asSequence()
        .filter { offers.containsKey(it.key) }
        .map { (product, quantity) ->
            val offer = offers.getValue(product)
            val unitPrice = catalog.getUnitPrice(product)
            offer.getDiscount(quantity.toInt(), unitPrice)
        }
        .filterNotNull()
        .forEach { addDiscount(it) }
}
