package kz.hashiroii.home

import android.net.Uri
import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.model.TransactionType
import java.time.LocalDate

sealed interface HomeIntent {

    data class OnSaveTransactions(val transactions: List<Transaction>) : HomeIntent
    data class OnDeleteTransaction(val id: Long) : HomeIntent
    data class OnLoadTransactionsByPeriod(val start: LocalDate, val end: LocalDate) : HomeIntent
    data class OnLoadTransactionsByType(val type: TransactionType) : HomeIntent
    data class OnLoadTransactionsById(val id: Long) : HomeIntent
    data class OnImportFile(val uri: Uri) : HomeIntent
    data object OnLoadAllTransactions : HomeIntent
    data object Retry : HomeIntent
}