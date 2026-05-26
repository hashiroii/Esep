package kz.hashiroii.home

import android.net.Uri
import kz.hashiroii.domain.model.PeriodType
import kz.hashiroii.domain.model.Transaction
import java.time.LocalDate

sealed interface HomeIntent {
    data class OnImportFile(val uri: Uri) : HomeIntent
    data class OnSaveTransactions(val transactions: List<Transaction>) : HomeIntent
    data class OnDeleteTransaction(val id: Long) : HomeIntent
    data object OnPeriodNext : HomeIntent
    data object OnPeriodPrevious : HomeIntent
    data class OnPeriodTypeChanged(val type: PeriodType) : HomeIntent
    data class OnCustomPeriod(val start: LocalDate, val end: LocalDate) : HomeIntent
}