package kz.hashiroii.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.hashiroii.domain.model.FinancialSummary
import kz.hashiroii.domain.model.TransactionCategory
import kz.hashiroii.ui.theme.PreviewTheme
import kz.hashiroii.ui.theme.expenseColor
import kz.hashiroii.ui.theme.incomeColor
import java.util.Locale

@Composable
fun SummarySection(
    summary: FinancialSummary,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DifferenceCard(summary)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MiniSummaryCard(
                label = "Income",
                amount = summary.totalIncome,
                color = MaterialTheme.colorScheme.incomeColor,
                modifier = Modifier.weight(1f)
            )
            MiniSummaryCard(
                label = "Expenses",
                amount = summary.totalExpenses,
                color = MaterialTheme.colorScheme.expenseColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DifferenceCard(summary: FinancialSummary) {
    val isPositive = summary.difference >= 0
    val diffColor = if (isPositive) MaterialTheme.colorScheme.incomeColor
    else MaterialTheme.colorScheme.expenseColor
    val sign = if (isPositive) "+" else ""

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Cash Flow",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$sign${summary.difference.formatAmount()}",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = diffColor
            )
        }
    }
}

@Composable
private fun MiniSummaryCard(
    label: String,
    amount: Double,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = amount.formatAmount(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
    }
}

// Formats e.g. 150000.0 → "150 000 ₸"
internal fun Double.formatAmount(): String {
    val formatted = String.format(Locale.US, "%,.0f", this).replace(",", " ")
    return "$formatted ₸"
}

private val previewSummary = FinancialSummary(
    totalIncome = 175000.0,
    totalExpenses = 63750.0,
    categoryBreakdown = mapOf(
        TransactionCategory.FOOD to 15000.0,
        TransactionCategory.GROCERIES to 25000.0,
        TransactionCategory.TRANSPORT to 8750.0,
        TransactionCategory.ENTERTAINMENT to 15000.0,
    )
)

@PreviewLightDark
@Composable
private fun SummarySectionPositivePreview() {
    PreviewTheme {
        SummarySection(
            summary = previewSummary,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@PreviewLightDark
@Composable
private fun SummarySectionNegativePreview() {
    PreviewTheme {
        SummarySection(
            summary = previewSummary.copy(totalIncome = 30000.0),
            modifier = Modifier.padding(16.dp)
        )
    }
}