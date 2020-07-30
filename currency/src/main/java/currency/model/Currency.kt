package currency.model

import kotlin.math.abs
import kotlin.math.roundToInt

sealed class Currency {
    internal abstract val totalCents: Int

    override fun toString(): String = simplify().toString()
}

data class Money(val dollars: Dollars, val cents: Cents) : Currency() {
    override val totalCents: Int = dollars.amount * 100 + cents.amount

    override fun toString(): String {
        return if (totalCents < 0) {
            abs(totalCents).cents.simplify().let { "-$${it.stringValueWithoutSign}" }
        } else {
            simplify().let { "$${it.stringValueWithoutSign}" }
        }
    }

    private val stringValueWithoutSign
        get() = "${dollars.amount}.${cents.amount.toString().padStart(2, '0')}"
}

data class Dollars(val amount: Int) : Currency() {
    override val totalCents: Int = amount * 100

    override fun toString(): String = super.toString()
}

data class Cents(val amount: Int) : Currency() {
    override val totalCents: Int = amount

    override fun toString(): String = super.toString()
}

val Int.dollars
    get() = Dollars(this)

val Int.cents
    get() = Cents(this)

infix fun Dollars.and(cents: Cents) = Money(this, cents)

operator fun Dollars.plus(dollars: Dollars) =
    Dollars(amount + dollars.amount)

operator fun Currency.plus(currency: Currency) = Money(0.dollars, (totalCents + currency.totalCents).cents).simplify()

operator fun Dollars.minus(dollars: Dollars) =
    Dollars(amount - dollars.amount)

operator fun Currency.minus(dollars: Currency) =
    when {
        this < dollars -> {
            (dollars.totalCents - totalCents).cents.simplify().let {
                Money(
                    (it.dollars.amount * -1).dollars,
                    (if (it.dollars.amount == 0) -it.cents.amount else it.cents.amount).cents
                )
            }
        }
        else -> this + (-1 * dollars)
    }

operator fun Currency.times(multiplier: Number) =
    Money(0.dollars, (totalCents * multiplier.toDouble()).roundToInt().cents).simplify()

operator fun Number.times(currency: Currency) = currency * this

operator fun Currency.div(divider: Number) = this * (1 / divider.toDouble())

operator fun Currency.compareTo(currency: Currency) = totalCents - currency.totalCents

private fun Currency.simplify() = Money((totalCents / 100).dollars, (totalCents % 100).cents)
