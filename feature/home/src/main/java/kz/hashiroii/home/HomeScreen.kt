package kz.hashiroii.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import kz.hashiroii.domain.model.FinancialSummary
import kz.hashiroii.domain.model.Period
import kz.hashiroii.domain.model.PeriodType
import kz.hashiroii.domain.model.TransactionCategory
import kz.hashiroii.ui.theme.PreviewTheme

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
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
internal fun HomeScreen(
    state: HomeUiState,
    onImportClick: () -> Unit,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onImportClick) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Import statement")
            }
        }
    ) { paddingValues ->
        when (state) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is HomeUiState.Success -> {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    contentPadding = paddingValues
                ) {
                    item {
                        SummarySection(
                            summary = state.summary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    item {
                        PeriodSelector(
                            period = state.period,
                            onPrevious = { onIntent(HomeIntent.OnPeriodPrevious) },
                            onNext = { onIntent(HomeIntent.OnPeriodNext) },
                            onPeriodTypeSelected = { onIntent(HomeIntent.OnPeriodTypeChanged(it)) },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    if (state.summary.categoryBreakdown.isNotEmpty()) {
                        item {
                            CategoryBreakdownSection(
                                breakdown = state.summary.categoryBreakdown,
                                totalExpenses = state.summary.totalExpenses,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                    if (state.transactions.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No transactions for this period.\nTap + to import a statement.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
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

private val previewSummary = FinancialSummary(
    totalIncome = 175000.0,
    totalExpenses = 63750.0,
    categoryBreakdown = mapOf(
        TransactionCategory.GROCERIES to 25000.0,
        TransactionCategory.FOOD to 15000.0,
        TransactionCategory.ENTERTAINMENT to 15000.0,
        TransactionCategory.TRANSPORT to 8750.0,
    )
)

@PreviewLightDark
@Composable
private fun HomeScreenLoadingPreview() {
    PreviewTheme {
        HomeScreen(state = HomeUiState.Loading, onImportClick = {}, onIntent = {})
    }
}

@PreviewLightDark
@Composable
private fun HomeScreenSuccessPreview() {
    PreviewTheme {
        HomeScreen(
            state = HomeUiState.Success(
                period = Period.forType(PeriodType.MONTH),
                summary = previewSummary,
                transactions = emptyList()
            ),
            onImportClick = {},
            onIntent = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun HomeScreenEmptyPreview() {
    PreviewTheme {
        HomeScreen(
            state = HomeUiState.Success(
                period = Period.forType(PeriodType.MONTH),
                summary = FinancialSummary(0.0, 0.0, emptyMap()),
                transactions = emptyList()
            ),
            onImportClick = {},
            onIntent = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun HomeScreenErrorPreview() {
    PreviewTheme {
        HomeScreen(
            state = HomeUiState.Error(Exception("Failed to load transactions")),
            onImportClick = {},
            onIntent = {}
        )
    }
}