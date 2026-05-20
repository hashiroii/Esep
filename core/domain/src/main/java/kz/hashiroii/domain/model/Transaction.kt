package kz.hashiroii.domain.model

import java.time.LocalDate

data class Transaction(
    val date: LocalDate, // change later
    val amount: Double,
    val isIncome: Boolean,
    val type: String,
    val merchant: String,
    val isBlocked: Boolean = false
)