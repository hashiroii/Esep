package kz.hashiroii.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kz.hashiroii.designsystem.theme.PreviewTheme
import kz.hashiroii.domain.model.TransactionCategory
import kotlin.math.roundToInt

@Composable
fun CategoryBreakdownSection(
    breakdown: Map<TransactionCategory, Double>,
    totalExpenses: Double,
    modifier: Modifier = Modifier
) {
    if (breakdown.isEmpty()) return

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Spending by Category",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                DonutChart(breakdown = breakdown, modifier = Modifier.size(160.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = totalExpenses.formatAmount(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                breakdown.forEach { (category, amount) ->
                    val pct = if (totalExpenses > 0) (amount / totalExpenses * 100).roundToInt() else 0
                    LegendItem(
                        color = category.chartColor,
                        label = category.displayName,
                        percent = pct
                    )
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String, percent: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$percent%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DonutChart(
    breakdown: Map<TransactionCategory, Double>,
    modifier: Modifier = Modifier
) {
    val total = breakdown.values.sum().toFloat()
    if (total == 0f) return

    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.18f
        val radius = (size.minDimension - strokeWidth) / 2f
        val topLeft = Offset((size.width - radius * 2) / 2f, (size.height - radius * 2) / 2f)
        val arcSize = Size(radius * 2, radius * 2)
        val gapDegrees = 2f
        var startAngle = -90f

        breakdown.forEach { (category, amount) ->
            val sweep = (amount.toFloat() / total * 360f) - gapDegrees
            drawArc(
                color = category.chartColor,
                startAngle = startAngle,
                sweepAngle = sweep.coerceAtLeast(0f),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
            )
            startAngle += sweep + gapDegrees
        }
    }
}

private val TransactionCategory.chartColor: Color
    get() = when (this) {
        TransactionCategory.FOOD -> Color(0xFFE57373)
        TransactionCategory.GROCERIES -> Color(0xFF81C784)
        TransactionCategory.TRANSPORT -> Color(0xFF64B5F6)
        TransactionCategory.FUEL -> Color(0xFFFFD54F)
        TransactionCategory.HEALTH -> Color(0xFF4DB6AC)
        TransactionCategory.ENTERTAINMENT -> Color(0xFFBA68C8)
        TransactionCategory.UTILITIES -> Color(0xFF4DD0E1)
        TransactionCategory.SHOPPING -> Color(0xFFFF8A65)
        TransactionCategory.TRANSFERS -> Color(0xFF90A4AE)
        TransactionCategory.OTHER -> Color(0xFFBDBDBD)
    }

@PreviewLightDark
@Composable
private fun CategoryBreakdownPreview() {
    PreviewTheme {
        CategoryBreakdownSection(
            breakdown = mapOf(
                TransactionCategory.GROCERIES to 25000.0,
                TransactionCategory.FOOD to 15000.0,
                TransactionCategory.ENTERTAINMENT to 15000.0,
                TransactionCategory.TRANSPORT to 8750.0,
            ),
            totalExpenses = 63750.0,
            modifier = Modifier.padding(16.dp)
        )
    }
}