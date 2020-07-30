package supermarket.model

class ShoppingCart {

    private val productQuantities: MutableMap<Product, Double> = mutableMapOf()

    fun getItems(): Map<Product, Double> = productQuantities

    fun addItem(product: Product) {
        this.addItemQuantity(product, 1.0)
    }

    fun addItemQuantity(product: Product, quantity: Double) {
        val productQuantity = productQuantities.getOrPut(product) { 0.0 }
        productQuantities[product] = productQuantity + quantity
    }
}
