package kz.hashiroii.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kz.hashiroii.domain.model.FinancialSummary
import kz.hashiroii.domain.model.Period
import kz.hashiroii.domain.model.PeriodType
import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.model.TransactionCategory
import kz.hashiroii.domain.parser.PdfTextExtractor
import kz.hashiroii.domain.usecase.ParsePdfUseCase
import kz.hashiroii.domain.usecase.transaction.DeleteTransactionByIdUseCase
import kz.hashiroii.domain.usecase.transaction.GetAllTransactionsUseCase
import kz.hashiroii.domain.usecase.transaction.SaveTransactionsUseCase
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pdfTextExtractor: PdfTextExtractor,
    private val parsePdfUseCase: ParsePdfUseCase,
    private val saveTransactionsUseCase: SaveTransactionsUseCase,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val deleteTransactionByIdUseCase: DeleteTransactionByIdUseCase
) : ViewModel() {

    private val _period = MutableStateFlow(Period.forType(PeriodType.MONTH))

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // combine() merges two flows into one. Every time _period changes OR Room
        // emits a new transaction list, this block re-runs and rebuilds the UI state.
        viewModelScope.launch {
            combine(
                _period,
                getAllTransactionsUseCase()
            ) { period, allTransactions ->
                buildSuccess(period, allTransactions) as HomeUiState
            }
                .catch { e -> emit(HomeUiState.Error(e as? Exception ?: Exception(e.message))) }
                .collect { _uiState.value = it }
        }
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.OnImportFile -> onImportFile(intent)
            is HomeIntent.OnSaveTransactions -> onSaveTransactions(intent.transactions)
            is HomeIntent.OnDeleteTransaction -> onDeleteTransaction(intent.id)
            is HomeIntent.OnPeriodNext -> _period.update { it.next() }
            is HomeIntent.OnPeriodPrevious -> _period.update { it.previous() }
            is HomeIntent.OnPeriodTypeChanged -> _period.value = Period.forType(intent.type)
            is HomeIntent.OnCustomPeriod -> _period.update { it.copy(start = intent.start, end = intent.end) }
        }
    }

    private fun onImportFile(intent: HomeIntent.OnImportFile) {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                val text = pdfTextExtractor.extract(intent.uri)
                val transactions = parsePdfUseCase(text)
                saveTransactionsUseCase(transactions)
                // No need to manually reload — combine() above will react to Room's new emission
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Import failed", e)
                _uiState.value = HomeUiState.Error(e)
            }
        }
    }

    private fun onSaveTransactions(transactions: List<Transaction>) {
        viewModelScope.launch {
            try {
                saveTransactionsUseCase(transactions)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e)
            }
        }
    }

    private fun onDeleteTransaction(id: Long) {
        viewModelScope.launch {
            try {
                deleteTransactionByIdUseCase(id)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e)
            }
        }
    }

    private fun buildSuccess(period: Period, allTransactions: List<Transaction>): HomeUiState.Success {
        val filtered = allTransactions.filter { tx ->
            !tx.date.isBefore(period.start) && !tx.date.isAfter(period.end)
        }
        val income = filtered.filter { it.isIncome }.sumOf { it.amount }
        val expenses = filtered.filter { !it.isIncome }.sumOf { it.amount }
        val breakdown = filtered
            .filter { !it.isIncome }
            .groupBy { it.category }
            .mapValues { (_, txs) -> txs.sumOf { it.amount } }
            .entries
            .sortedByDescending { it.value }
            .associate { it.key to it.value }

        return HomeUiState.Success(
            period = period,
            summary = FinancialSummary(income, expenses, breakdown),
            transactions = filtered
        )
    }
}