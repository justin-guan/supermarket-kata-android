package supermarket

import currency.model.and
import currency.model.cents
import currency.model.dollars
import org.junit.Test
import supermarket.model.Product
import supermarket.model.ProductUnit
import supermarket.model.ShoppingCart
import supermarket.model.SpecialOfferType
import supermarket.model.Teller

class SupermarketTest {

    @Test
    fun testSomething() {
        val catalog = FakeCatalog()
        val toothbrush = Product("toothbrush", ProductUnit.Each)
        catalog.addProduct(toothbrush, 99.cents)
        val apples = Product("apples", ProductUnit.Kilo)
        catalog.addProduct(apples, 1.dollars and 99.cents)

        val cart = ShoppingCart()
        cart.addItemQuantity(apples, 2.5)
        cart.addItemQuantity(toothbrush, 1.0)

        val teller = Teller(catalog)
        teller.addSpecialOffer(SpecialOfferType.PercentageDiscount(10.0), toothbrush)

        val receipt = teller.checksOutArticlesFrom(cart)

        // TODO: This just prints a receipt to give you an idea how the code works.
        println(ReceiptPrinter().printReceipt(receipt))
    }
}
