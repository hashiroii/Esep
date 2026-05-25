package kz.hashiroii.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.model.TransactionType
import kz.hashiroii.ui.component.transaction.TransactionCard
import kz.hashiroii.ui.theme.PreviewTheme
import java.time.LocalDate

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { viewModel.onIntent(HomeIntent.OnImportFile(it)) }
    }

    HomeScreen(
        state = state,
        onImportClick = { filePicker.launch(arrayOf("application/pdf")) },
        modifier = modifier
    )
}

@Composable
internal fun HomeScreen(
    state: HomeUiState,
    onImportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold { paddingValues ->
        when (state) {
            is HomeUiState.Loading -> {
                Box(modifier = modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is HomeUiState.Success -> {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    contentPadding = paddingValues
                ) {
                    item {
                        ImportCard(
                            onClick = onImportClick,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                    }
                    items(state.transactions) { transaction ->
                        TransactionCard(
                            transaction = transaction,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            is HomeUiState.Error -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.e.message ?: "Something went wrong",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

private val previewTransactions = listOf(
    Transaction(
        id = 0,
        date = LocalDate.of(2026, 5, 1),
        amount = 150000.0,
        isIncome = true,
        type = TransactionType.REPLENISHMENT,
        merchant = "Salary"
    ),
    Transaction(
        id = 1,
        date = LocalDate.of(2026, 5, 5),
        amount = 3750.0,
        isIncome = false,
        type = TransactionType.PURCHASES,
        merchant = "Magnum"
    ),
    Transaction(
        id = 2,
        date = LocalDate.of(2026, 5, 10),
        amount = 10000.0,
        isIncome = false,
        type = TransactionType.TRANSFERS,
        merchant = "Kaspi Transfer"
    ),
    Transaction(
        id = 3,
        date = LocalDate.of(2026, 5, 15),
        amount = 1200.0,
        isIncome = false,
        type = TransactionType.PURCHASES,
        merchant = "Starbucks"
    ),
    Transaction(
        id = 4,
        date = LocalDate.of(2026, 5, 20),
        amount = 25000.0,
        isIncome = true,
        type = TransactionType.REPLENISHMENT,
        merchant = "Freelance"
    ),
)

@PreviewLightDark
@Composable
private fun HomeScreenLoadingPreview() {
    PreviewTheme {
        HomeScreen(state = HomeUiState.Loading, onImportClick = {})
    }
}

@PreviewLightDark
@Composable
private fun HomeScreenSuccessPreview() {
    PreviewTheme {
        HomeScreen(
            state = HomeUiState.Success(transactions = previewTransactions),
            onImportClick = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun HomeScreenErrorPreview() {
    PreviewTheme {
        HomeScreen(
            state = HomeUiState.Error(Exception("Failed to load transactions")),
            onImportClick = {}
        )
    }
}
