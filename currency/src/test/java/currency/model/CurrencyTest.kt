package currency.model

import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is` as Is
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CurrencyTest {

    @Test
    fun `add two money amounts without cents carryover`() {
        val currency = (2.dollars and 3.cents) + (3.dollars and 4.cents)

        assertThat(currency.dollars.amount, Is(5))
        assertThat(currency.cents.amount, Is(7))
    }

    @Test
    fun `add two money amounts with cents carryover`() {
        val currency = (2.dollars and 100.cents) + (3.dollars and 4.cents)

        assertThat(currency.dollars.amount, Is(6))
        assertThat(currency.cents.amount, Is(4))
    }

    @Test
    fun `add two dollar amounts monetary values in a dollar amount`() {
        val currency = 1.dollars + 2.dollars

        assertThat(currency, instanceOf(Dollars::class.java))
        assertThat(currency.amount, Is(3))
    }

    @Test
    fun `add dollars and cents monetary values in a currency value`() {
        val currency = 1.dollars + 2.cents

        assertThat(currency.dollars.amount, Is(1))
        assertThat(currency.cents.amount, Is(2))
    }

    @Test
    fun `add two cents values monetary values in a currency value without carryover`() {
        val currency = 1.cents + 2.cents

        assertThat(currency.dollars.amount, Is(0))
        assertThat(currency.cents.amount, Is(3))
    }

    @Test
    fun `add two cents values monetary values in a currency value with carryover`() {
        val currency = 99.cents + 2.cents

        assertThat(currency.dollars.amount, Is(1))
        assertThat(currency.cents.amount, Is(1))
    }

    @Test
    fun `subtracting two dollar values results in a smaller dollar value`() {
        val currency = 10.dollars - 9.dollars

        assertThat(currency, instanceOf(Dollars::class.java))
        assertThat(currency.amount, Is(1))
    }

    @Test
    fun `subtracting two monetary values results in a smaller monetary value`() {
        val currency = (10.dollars and 50.cents) - (9.dollars and 25.cents)

        assertThat(currency.dollars.amount, Is(1))
        assertThat(currency.cents.amount, Is(25))
    }

    @Test
    fun `subtracting two monetary values results in a smaller monetary value with carryover`() {
        val currency = (10.dollars and 50.cents) - (9.dollars and 51.cents)

        assertThat(currency.dollars.amount, Is(0))
        assertThat(currency.cents.amount, Is(99))
    }

    @Test
    fun `subtracting two monetary values results in a negative cents value`() {
        val currency = (5.dollars and 25.cents) - (6.dollars and 20.cents)

        assertThat(currency.dollars.amount, Is(0))
        assertThat(currency.cents.amount, Is(-95))
    }

    @Test
    fun `subtracting two monetary values results in a negative dollar value`() {
        val currency = (5.dollars and 25.cents) - (7.dollars and 20.cents)

        assertThat(currency.dollars.amount, Is(-1))
        assertThat(currency.cents.amount, Is(95))
    }

    @Test
    fun `subtracting two equal monetary values results in a zero value`() {
        val value = 123.dollars and 94.cents

        with(value - value) {
            assertThat(dollars.amount, Is(0))
            assertThat(cents.amount, Is(0))
        }
    }

    @Test
    fun `multiplying monetary values results in a value without cents`() {
        val currency = 2.dollars * 5

        assertThat(currency.dollars.amount, Is(10))
        assertThat(currency.cents.amount, Is(0))
    }

    @Test
    fun `multiplying monetary values results in a value that has cents`() {
        val currency = 2.dollars * 5.6

        assertThat(currency.dollars.amount, Is(11))
        assertThat(currency.cents.amount, Is(20))
    }

    @Test
    fun `multiplying a monetary value results in a decimal that is rounded`() {
        val currency = 2.dollars * 5.631 // $11.262 -> 11.26

        assertThat(currency.dollars.amount, Is(11))
        assertThat(currency.cents.amount, Is(26))
    }

    @Test
    fun `multiplying monetary value with number is commutative`() {
        val currency = 5.631 * 2.dollars // $11.262 -> 11.26

        assertThat(currency.dollars.amount, Is(11))
        assertThat(currency.cents.amount, Is(26))
    }

    @Test
    fun `dividing a monetary value with a number does not split into cents`() {
        val currency = 10.dollars / 2

        assertThat(currency.dollars.amount, Is(5))
        assertThat(currency.cents.amount, Is(0))
    }

    @Test
    fun `dividing a monetary value with a number splits into cents`() {
        val currency = (10.dollars and 50.cents) / 2

        assertThat(currency.dollars.amount, Is(5))
        assertThat(currency.cents.amount, Is(25))
    }

    @Test
    fun `dividing a monetary value with a number results in the cents value being rounded`() {
        val currency = (10.dollars and 25.cents) / 2

        assertThat(currency.dollars.amount, Is(5))
        assertThat(currency.cents.amount, Is(13))
    }

    @Test
    fun `comparing two equal monetary values results in equality`() {
        val value = 10.dollars and 10.cents

        assertThat(value == value.copy(), Is(true))
    }

    @Test
    fun `comparing two unequal monetary values correctly identifies the larger monetary value`() {
        val value = 10.dollars and 10.cents

        assertThat(value > value.copy(dollars = 9.dollars), Is(true))
    }

    @Test
    fun `comparing two unequal monetary values correctly identifies the smaller monetary value`() {
        val value = 10.dollars and 10.cents

        assertThat(value < value.copy(dollars = 11.dollars), Is(true))
    }

    @Test
    fun `string representation of money is correct`() {
        val money = 12.dollars and 11.cents

        assertThat(money.toString(), Is("$12.11"))
    }

    @Test
    fun `string representation of money simplifies cents into dollars first`() {
        val money = 12.dollars and 101.cents

        assertThat(money.toString(), Is("$13.01"))
    }

    @Test
    fun `string representation of money shows negative value`() {
        val money = (-10).dollars

        assertThat(money.toString(), Is("-$10.00"))
    }

    @Test
    fun `string representation of cents shows exactly two digits`() {
        val money = 1.cents

        assertThat(money.toString(), Is("$0.01"))
    }
}
