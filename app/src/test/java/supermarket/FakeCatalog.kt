package supermarket

import currency.model.Currency
import supermarket.model.Product
import supermarket.model.SupermarketCatalog

class FakeCatalog : SupermarketCatalog {
    private val products = HashMap<String, Product>()
    private val prices = HashMap<String, Currency>()

    override fun addProduct(product: Product, price: Currency) {
        this.products[product.name] = product
        this.prices[product.name] = price
    }

    override fun getUnitPrice(product: Product): Currency {
        return this.prices[product.name]!!
    }
}
