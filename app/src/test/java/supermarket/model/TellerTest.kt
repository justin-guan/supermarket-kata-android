package supermarket.model

import currency.model.Currency
import currency.model.and
import currency.model.cents
import currency.model.div
import currency.model.dollars
import currency.model.minus
import currency.model.times
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import supermarket.FakeCatalog
import org.hamcrest.CoreMatchers.`is` as Is

private val TEST_PRODUCT_PRICE = 9.dollars and 99.cents

class TellerTest {

    private val testProduct = Product("test product", ProductUnit.Each)
    private val catalog = FakeCatalog().apply {
        addProduct(testProduct, TEST_PRODUCT_PRICE)
    }

    @Test
    fun `checkout without adding offers generates receipt with no discounts`() {
        val productQuantity = 5.0
        val cart = ShoppingCart().apply {
            addItemQuantity(testProduct, productQuantity)
        }
        val teller = Teller(catalog)

        val receipt = teller.checksOutArticlesFrom(cart)

        assertThat(receipt.totalPrice, Is<Currency>(TEST_PRODUCT_PRICE * productQuantity))
        assertThat(receipt.getDiscounts().isEmpty(), Is(true))
        assertThat(receipt.getItems().size, Is(1))
        with(receipt.getItems().first()) {
            assertThat(price, Is<Currency>(TEST_PRODUCT_PRICE))
            assertThat(product, Is(testProduct))
            assertThat(quantity, Is(productQuantity))
        }
    }

    @Test
    fun `checkout with percentage discount offer generates receipt with discounts`() {
        val productQuantity = 5.0
        val cart = ShoppingCart().apply {
            addItemQuantity(testProduct, productQuantity)
        }
        val teller = Teller(catalog).apply {
            addSpecialOffer(SpecialOfferType.PercentageDiscount(10.0), testProduct)
        }

        val receipt = teller.checksOutArticlesFrom(cart)

        val totalPriceBeforeDiscount = productQuantity * TEST_PRODUCT_PRICE
        val discount = totalPriceBeforeDiscount / 10
        val expectedPrice = totalPriceBeforeDiscount - discount
        assertThat(receipt.totalPrice, Is<Currency>(expectedPrice))
        assertThat(receipt.getDiscounts().size, Is(1))
        with(receipt.getDiscounts().first()) {
            assertThat(product, Is(testProduct))
            assertThat(discountAmount, Is<Currency>(discount))
        }
        assertThat(receipt.getItems().size, Is(1))
        with(receipt.getItems().first()) {
            assertThat(price, Is<Currency>(TEST_PRODUCT_PRICE))
            assertThat(product, Is(testProduct))
            assertThat(quantity, Is(productQuantity))
        }
    }

    @Test
    fun `checkout with quantity for amount discount offer generates receipt with discounts`() {
        val productQuantity = 5.0
        val cart = ShoppingCart().apply {
            addItemQuantity(testProduct, productQuantity)
        }
        val teller = Teller(catalog).apply {
            addSpecialOffer(SpecialOfferType.QuantityForAmount(2.0, TEST_PRODUCT_PRICE), testProduct)
        }

        val receipt = teller.checksOutArticlesFrom(cart)

        assertThat(receipt.totalPrice, Is<Currency>(29.dollars and 97.cents))
        assertThat(receipt.getDiscounts().size, Is(1))
        with(receipt.getDiscounts().first()) {
            assertThat(product, Is(testProduct))
            assertThat(discountAmount, Is<Currency>(19.dollars and 98.cents))
        }
        assertThat(receipt.getItems().size, Is(1))
        with(receipt.getItems().first()) {
            assertThat(price, Is<Currency>(TEST_PRODUCT_PRICE))
            assertThat(product, Is(testProduct))
            assertThat(quantity, Is(productQuantity))
        }
    }
}
