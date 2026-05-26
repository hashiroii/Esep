package kz.hashiroii.ui.component.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.model.TransactionType
import kz.hashiroii.designsystem.theme.PreviewTheme
import kz.hashiroii.designsystem.theme.expenseColor
import kz.hashiroii.designsystem.theme.incomeColor
import java.time.LocalDate

@Composable
fun TransactionCard(
    modifier: Modifier = Modifier,
    transaction: Transaction
) {
    val isIncome = transaction.isIncome
    val amountColor = if (isIncome) MaterialTheme.colorScheme.incomeColor else MaterialTheme.colorScheme.expenseColor
    val amountPrefix = if (isIncome) "+" else "-"
    val arrowIcon = if (isIncome) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward
    val typeLabel = transaction.type.name
        .lowercase()
        .replaceFirstChar { it.uppercase() }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = transaction.merchant,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = typeLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = transaction.date.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = arrowIcon,
                    contentDescription = null,
                    tint = amountColor,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$amountPrefix${transaction.amount} ₸",
                    style = MaterialTheme.typography.titleLarge,
                    color = amountColor
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun TransactionCardIncomePreview() {
    PreviewTheme {
        TransactionCard(
            modifier = Modifier.padding(16.dp),
            transaction = Transaction(
                id = 0,
                date = LocalDate.of(2026, 5, 1),
                amount = 150000.0,
                isIncome = true,
                type = TransactionType.REPLENISHMENT,
                merchant = "Salary"
            )
        )
    }
}

@PreviewLightDark
@Composable
private fun TransactionCardExpensePreview() {
    PreviewTheme {
        TransactionCard(
            modifier = Modifier.padding(16.dp),
            transaction = Transaction(
                id = 1,
                date = LocalDate.of(2026, 5, 15),
                amount = 3750.0,
                isIncome = false,
                type = TransactionType.PURCHASES,
                merchant = "Magnum"
            )
        )
    }
}

@PreviewLightDark
@Composable
private fun TransactionCardTransferPreview() {
    PreviewTheme {
        TransactionCard(
            modifier = Modifier.padding(16.dp),
            transaction = Transaction(
                id = 2,
                date = LocalDate.of(2026, 5, 20),
                amount = 10000.0,
                isIncome = false,
                type = TransactionType.TRANSFERS,
                merchant = "Kaspi Transfer"
            )
        )
    }
}