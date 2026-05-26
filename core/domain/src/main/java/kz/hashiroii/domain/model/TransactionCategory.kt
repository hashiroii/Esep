package kz.hashiroii.domain.model

enum class TransactionCategory(val displayName: String) {
    FOOD("Food & Drinks"),
    GROCERIES("Groceries"),
    TRANSPORT("Transport"),
    FUEL("Fuel"),
    HEALTH("Health"),
    ENTERTAINMENT("Entertainment"),
    UTILITIES("Utilities"),
    SHOPPING("Shopping"),
    TRANSFERS("Transfers"),
    OTHER("Other")
}