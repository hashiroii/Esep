package kz.hashiroii.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.model.TransactionType
import kz.hashiroii.domain.usecase.ParsePdfUseCase
import kz.hashiroii.domain.usecase.PdfTextExtractor
import kz.hashiroii.domain.usecase.transaction.DeleteTransactionByIdUseCase
import kz.hashiroii.domain.usecase.transaction.GetAllTransactionsUseCase
import kz.hashiroii.domain.usecase.transaction.GetTransactionsByIdUseCase
import kz.hashiroii.domain.usecase.transaction.GetTransactionsByPeriodUseCase
import kz.hashiroii.domain.usecase.transaction.GetTransactionsByTypeUseCase
import kz.hashiroii.domain.usecase.transaction.SaveTransactionsUseCase
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pdfTextExtractor: PdfTextExtractor,
    private val parsePdfUseCase: ParsePdfUseCase,
    private val saveTransactions: SaveTransactionsUseCase,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getTransactionsByIdUseCase: GetTransactionsByIdUseCase,
    private val getTransactionsByPeriodUseCase: GetTransactionsByPeriodUseCase,
    private val getTransactionsByTypeUseCase: GetTransactionsByTypeUseCase,
    private val deleteTransactionByIdUseCase: DeleteTransactionByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        onLoadAllTransactions()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.Retry -> onLoadAllTransactions()
            is HomeIntent.OnImportFile -> onImportFile(intent)
            is HomeIntent.OnSaveTransactions -> onSaveTransactions(intent.transactions)
            is HomeIntent.OnDeleteTransaction -> onDeleteTransaction(intent.id)
            is HomeIntent.OnLoadAllTransactions -> onLoadAllTransactions()
            is HomeIntent.OnLoadTransactionsById -> onLoadTransactionsById(intent.id)
            is HomeIntent.OnLoadTransactionsByPeriod -> onLoadTransactionsByPeriod(intent.start, intent.end)
            is HomeIntent.OnLoadTransactionsByType -> onLoadTransactionsByType(intent.type)
        }
    }

    private fun onImportFile(intent: HomeIntent.OnImportFile) {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                val text = pdfTextExtractor.extract(intent.uri)
                val transactions = parsePdfUseCase(text)
                saveTransactions(transactions)
                onLoadAllTransactions()
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Import failed", e)
                _uiState.value = HomeUiState.Error(e)
            }
        }
    }

    private fun onSaveTransactions(transactions: List<Transaction>) {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                saveTransactions(transactions)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e)
            }
        }
    }

    private fun onDeleteTransaction(id: Long) {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                deleteTransactionByIdUseCase(id)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e)
            }
        }
    }

    private fun onLoadAllTransactions() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                getAllTransactionsUseCase().collect { transactions ->
                    _uiState.value = HomeUiState.Success(transactions = transactions)
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e)
            }
        }
    }

    private fun onLoadTransactionsById(id: Long) {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                getTransactionsByIdUseCase(id).collect { transactions ->
                    _uiState.value = HomeUiState.Success(transactions = transactions)
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e)
            }
        }
    }

    private fun onLoadTransactionsByType(type: TransactionType) {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                getTransactionsByTypeUseCase(type).collect { transactions ->
                    _uiState.value = HomeUiState.Success(transactions = transactions)
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e)
            }
        }
    }

    private fun onLoadTransactionsByPeriod(start: LocalDate, end: LocalDate) {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                getTransactionsByPeriodUseCase(start, end).collect { transactions ->
                    _uiState.value = HomeUiState.Success(transactions = transactions)
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e)
            }
        }
    }
}