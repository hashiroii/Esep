package kz.hashiroii.domain.model

import java.time.LocalDate

data class Transaction(
    val id: Long,
    val date: LocalDate,
    val amount: Double,
    val isIncome: Boolean,
    val type: TransactionType,
    val merchant: String,
    val category: TransactionCategory = TransactionCategory.OTHER
)

enum class TransactionType {
    PURCHASES,
    TRANSFERS,
    REPLENISHMENT,
    OTHERS,
    UNKNOWN
}