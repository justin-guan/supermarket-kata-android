package currency.model

import kotlin.math.roundToInt

sealed class Currency {
    internal abstract val totalCents: Int
}

data class Money(val dollars: Dollars, val cents: Cents) : Currency() {
    override val totalCents: Int = dollars.amount * 100 + cents.amount
}

data class Dollars(val amount: Int) : Currency() {
    override val totalCents: Int = amount * 100
}

data class Cents(val amount: Int) : Currency() {
    override val totalCents: Int = amount
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
            val inverseAmount = (dollars.totalCents - totalCents).cents.simplify()
            inverseAmount.copy(
                dollars = (inverseAmount.dollars.amount * -1).dollars,
                cents = (if (inverseAmount.dollars.amount == 0) -inverseAmount.cents.amount else inverseAmount.cents.amount).cents
            )
        }
        else -> this + (-1 * dollars)
    }

operator fun Currency.times(multiplier: Number) =
    Money(0.dollars, (totalCents * multiplier.toDouble()).roundToInt().cents).simplify()

operator fun Number.times(currency: Currency) = currency * this

operator fun Currency.div(divider: Number) = this * (1 / divider.toDouble())

operator fun Currency.compareTo(currency: Currency) = totalCents - currency.totalCents

private fun Currency.simplify() = Money((totalCents / 100).dollars, (totalCents % 100).cents)
