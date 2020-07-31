package supermarket

import com.accenture.utils.whitespaceOfSize
import supermarket.model.ProductUnit
import supermarket.model.Receipt
import supermarket.model.ReceiptItem
import java.util.*

class ReceiptPrinter @JvmOverloads constructor(private val columns: Int = 40) {

    fun printReceipt(receipt: Receipt): String {
        val result = StringBuilder()
        for (item in receipt.getItems()) {
            val price = item.totalPrice.toString()
            val quantity = presentQuantity(item)
            val name = item.product.name
            val unitPrice = item.price.toString()

            val whitespaceSize = this.columns - name.length - price.length
            var line = name + whitespaceOfSize(whitespaceSize) + price + "\n"

            if (item.quantity != 1.0) {
                line += "  $unitPrice * $quantity\n"
            }
            result.append(line)
        }
        for (discount in receipt.getDiscounts()) {
            val productPresentation = discount.product.name
            val pricePresentation = discount.discountAmount.toString()
            val description = discount.description
            val whitespaceSize = this.columns - 3 - productPresentation.length - description.length - pricePresentation.length
            result.append("$description($productPresentation)${whitespaceOfSize(whitespaceSize)}-$pricePresentation\n")
        }
        result.append("\n")
        val pricePresentation = receipt.totalPrice.toString()
        val total = "Total: "
        val whitespace = whitespaceOfSize(this.columns - total.length - pricePresentation.length)
        result.append(total).append(whitespace).append(pricePresentation)
        return result.toString()
    }

    private fun presentQuantity(item: ReceiptItem): String {
        return when(item.product.unit) {
            ProductUnit.Each -> String.format(Locale.ROOT, "%x", item.quantity.toInt())
            ProductUnit.Kilo -> String.format(Locale.ROOT, "%.3f", item.quantity)
        }
    }
}
