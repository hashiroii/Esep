package kz.hashiroii.home

import kz.hashiroii.domain.model.Transaction

sealed class HomeUiState {
    object Loading : HomeUiState()
    class Success(
        val transactions: List<Transaction>
    ) : HomeUiState()
    class Error(val e: Exception) : HomeUiState()
}