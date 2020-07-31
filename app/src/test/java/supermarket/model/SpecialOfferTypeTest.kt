package supermarket.model

import currency.model.and
import currency.model.cents
import currency.model.dollars
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.CoreMatchers.`is` as Is
import org.junit.Assert.*
import org.junit.Test

class SpecialOfferTypeTest {

    @Test
    fun `percentage discount for quantity of one`() {
        val testProduct = Product("product", ProductUnit.Each)
        val tenPercentDiscount = SpecialOfferType.PercentageDiscount(10.0)

        val discount = tenPercentDiscount.calculateDiscount(testProduct, 1, 100.dollars)

        assertThat(discount, Is(Discount(testProduct, "10.0% off", 10.dollars and 0.cents)))
    }

    @Test
    fun `percentage discount for multiple quantities`() {
        val testProduct = Product("product", ProductUnit.Each)
        val tenPercentDiscount = SpecialOfferType.PercentageDiscount(10.0)

        val discount = tenPercentDiscount.calculateDiscount(testProduct, 2, 100.dollars)

        assertThat(discount, Is(Discount(testProduct, "10.0% off", 20.dollars and 0.cents)))
    }

    @Test
    fun `discount not applied if required quantity is not met`() {
        val testProduct = Product("product", ProductUnit.Each)
        val specialOffer = SpecialOfferType.QuantityForAmount(    2.0, 1.dollars)

        val discount = specialOffer.calculateDiscount(testProduct, 1, 100.dollars)

        assertThat(discount, Is(nullValue()))
    }

    @Test
    fun `price is set to a specific amount if exact quantity is met`() {
        val testProduct = Product("product", ProductUnit.Each)
        val specialOffer = SpecialOfferType.QuantityForAmount(    2.0, 1.dollars)

        val discount = specialOffer.calculateDiscount(testProduct, 2, 100.dollars)

        assertThat(discount, Is(Discount(testProduct, "2.0 for $1.00", 199.dollars and 0.cents)))
    }

    @Test
    fun `discount is only applied to evenly distributed required quantities`() {
        val testProduct = Product("product", ProductUnit.Each)
        val specialOffer = SpecialOfferType.QuantityForAmount(    2.0, 1.dollars)

        val discount = specialOffer.calculateDiscount(testProduct, 3, 100.dollars)

        assertThat(discount, Is(Discount(testProduct, "2.0 for $1.00", 199.dollars and 0.cents)))
    }
}
