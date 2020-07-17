package supermarket.model

sealed class SpecialOfferType {
    val description: String
        get() = this::class.java.simpleName

    sealed class Requirement : SpecialOfferType() {
        object ThreeForTwo : Requirement()
        object TwoForAmount : Requirement()
        object FiveForAmount : Requirement()
    }

    sealed class NoRequirement : SpecialOfferType() {
        object TenPercentDiscount : NoRequirement()
    }
}
