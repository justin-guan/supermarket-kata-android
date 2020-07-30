package supermarket.model

import currency.model.Currency

interface SupermarketCatalog {
    fun addProduct(product: Product, price: Currency)

    fun getUnitPrice(product: Product): Currency
}
