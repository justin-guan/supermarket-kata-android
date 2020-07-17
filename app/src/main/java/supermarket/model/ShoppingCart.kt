package supermarket.model

import java.util.ArrayList
import java.util.HashMap

class ShoppingCart {

    private val items = ArrayList<ProductQuantity>()
    internal var productQuantities: MutableMap<Product, Double> = HashMap()


    internal fun getItems(): List<ProductQuantity> {
        return ArrayList(items)
    }

    internal fun addItem(product: Product) {
        this.addItemQuantity(product, 1.0)
    }

    internal fun productQuantities(): Map<Product, Double> {
        return productQuantities
    }


    fun addItemQuantity(product: Product, quantity: Double) {
        items.add(ProductQuantity(product, quantity))
        if (productQuantities.containsKey(product)) {
            productQuantities[product] = productQuantities[product]!! + quantity
        } else {
            productQuantities[product] = quantity
        }
    }

    internal fun handleOffers(receipt: Receipt, offers: Map<Product, Offer>, catalog: SupermarketCatalog) {
        for (p in productQuantities().keys) {
            val quantity = productQuantities[p]!!
            if (offers.containsKey(p)) {
                val offer = offers[p]!!
                val unitPrice = catalog.getUnitPrice(p)
                val quantityAsInt = quantity.toInt()

                val discount = when (offer.offerType) {
                    is SpecialOfferType.Requirement -> applyRequirementOffer(offer.offerType, p, offer, quantityAsInt, unitPrice)
                    is SpecialOfferType.NoRequirement -> applyNoRequirementOffer(p, offer, quantityAsInt, unitPrice)
                }

                if (discount != null)
                    receipt.addDiscount(discount)
            }

        }
    }

    private fun applyRequirementOffer(
        offerType: SpecialOfferType.Requirement,
        product: Product,
        offer: Offer,
        quantity: Int,
        unitPrice: Double
    ): Discount? {
        val requiredQuantity = when (offerType) {
            SpecialOfferType.Requirement.ThreeForTwo -> 3
            SpecialOfferType.Requirement.TwoForAmount -> 2
            SpecialOfferType.Requirement.FiveForAmount -> 5
        }
        val discountAmount = if (quantity >= requiredQuantity) {
            val totalAfterDiscount = offer.argument * (quantity / requiredQuantity) + quantity % requiredQuantity * unitPrice
            val totalWithoutDiscount = quantity * unitPrice
            totalWithoutDiscount - totalAfterDiscount
        } else {
            return null
        }
        return Discount(product, offerType.description, discountAmount)
    }

    private fun applyNoRequirementOffer(
        product: Product,
        offer: Offer,
        quantity: Int,
        unitPrice: Double
    ) = Discount(product, offer.argument.toString() + "% off", quantity * unitPrice * offer.argument / 100.0)
}
