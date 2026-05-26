package kz.hashiroii.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kz.hashiroii.domain.model.Period
import kz.hashiroii.domain.model.PeriodType
import kz.hashiroii.ui.theme.PreviewTheme

@Composable
fun PeriodSelector(
    period: Period,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onPeriodTypeSelected: (PeriodType) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevious) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Previous period"
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = period.label,
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Choose period",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        IconButton(onClick = onNext) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Next period"
            )
        }
    }

    if (showDialog) {
        PeriodTypeDialog(
            current = period.type,
            onSelect = { type ->
                onPeriodTypeSelected(type)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
private fun PeriodTypeDialog(
    current: PeriodType,
    onSelect: (PeriodType) -> Unit,
    onDismiss: () -> Unit
) {
    val options = listOf(PeriodType.DAY, PeriodType.WEEK, PeriodType.MONTH, PeriodType.YEAR)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select period") },
        text = {
            Column {
                options.forEach { type ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(type) }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = type == current,
                            onClick = { onSelect(type) }
                        )
                        Text(
                            text = type.displayName,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@PreviewLightDark
@Composable
private fun PeriodSelectorPreview() {
    PreviewTheme {
        PeriodSelector(
            period = Period.forType(PeriodType.MONTH),
            onPrevious = {},
            onNext = {},
            onPeriodTypeSelected = {}
        )
    }
}