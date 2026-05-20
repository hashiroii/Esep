package kz.hashiroii.domain.model

import java.time.LocalDate

data class Transaction(
    val id: Long,
    val date: LocalDate,
    val amount: Double,
    val isIncome: Boolean,
    val type: TransactionType,
    val merchant: String
)

enum class TransactionType {
    PURCHASES,
    TRANSFERS,
    REPLENISHMENT,
    OTHERS,
    UNKNOWN
}