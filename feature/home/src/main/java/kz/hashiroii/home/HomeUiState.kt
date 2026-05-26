package kz.hashiroii.home

import kz.hashiroii.domain.model.FinancialSummary
import kz.hashiroii.domain.model.Period
import kz.hashiroii.domain.model.Transaction

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val period: Period,
        val summary: FinancialSummary,
        val transactions: List<Transaction>
    ) : HomeUiState()
    data class Error(val e: Exception) : HomeUiState()
}