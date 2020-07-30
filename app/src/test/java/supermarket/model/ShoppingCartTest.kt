package supermarket.model

import org.hamcrest.CoreMatchers.`is` as Is
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.*
import org.junit.Test

class ShoppingCartTest {

    private val testProduct = Product("product", ProductUnit.Each)

    @Test
    fun `adding new item to cart creates new entry in cart`() {
        val shoppingCart = ShoppingCart()

        assertThat(shoppingCart.getItems()[testProduct], nullValue())

        shoppingCart.addItem(testProduct)

        assertThat(shoppingCart.getItems()[testProduct], Is(1.0))
    }

    @Test
    fun `adding an item already in cart will increase the number of the product in cart`() {
        val shoppingCart = ShoppingCart()

        shoppingCart.addItem(testProduct)

        assertThat(shoppingCart.getItems()[testProduct], Is(1.0))

        shoppingCart.addItem(testProduct)

        assertThat(shoppingCart.getItems()[testProduct], Is(2.0))
    }

    @Test
    fun `adding multiple of a new item adds that item with that quantity to the cart`() {
        val shoppingCart = ShoppingCart()

        shoppingCart.addItemQuantity(testProduct, 100.0)

        assertThat(shoppingCart.getItems()[testProduct], Is(100.0))
    }

    @Test
    fun `adding multiple of a existing item increases the quantity of that item in the cart by the amount`() {
        val shoppingCart = ShoppingCart()

        shoppingCart.addItemQuantity(testProduct, 100.0)

        assertThat(shoppingCart.getItems()[testProduct], Is(100.0))

        shoppingCart.addItemQuantity(testProduct, 100.0)

        assertThat(shoppingCart.getItems()[testProduct], Is(200.0))
    }
}
