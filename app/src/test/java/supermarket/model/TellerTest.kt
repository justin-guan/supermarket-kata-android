package supermarket.model

import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import supermarket.FakeCatalog
import org.hamcrest.CoreMatchers.`is` as Is

private const val TEST_PRODUCT_PRICE = 10.0

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

        assertThat(receipt.totalPrice, Is(TEST_PRODUCT_PRICE * productQuantity))
        assertThat(receipt.getDiscounts().isEmpty(), Is(true))
        assertThat(receipt.getItems().size, Is(1))
        with(receipt.getItems().first()) {
            assertThat(price, Is(TEST_PRODUCT_PRICE))
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

        assertThat(receipt.totalPrice, Is(45.0))
        assertThat(receipt.getDiscounts().size, Is(1))
        with(receipt.getDiscounts().first()) {
            assertThat(product, Is(testProduct))
            assertThat(discountAmount, Is(5.0))
        }
        assertThat(receipt.getItems().size, Is(1))
        with(receipt.getItems().first()) {
            assertThat(price, Is(TEST_PRODUCT_PRICE))
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

        assertThat(receipt.totalPrice, Is(30.0))
        assertThat(receipt.getDiscounts().size, Is(1))
        with(receipt.getDiscounts().first()) {
            assertThat(product, Is(testProduct))
            assertThat(discountAmount, Is(20.0))
        }
        assertThat(receipt.getItems().size, Is(1))
        with(receipt.getItems().first()) {
            assertThat(price, Is(TEST_PRODUCT_PRICE))
            assertThat(product, Is(testProduct))
            assertThat(quantity, Is(productQuantity))
        }
    }
}
