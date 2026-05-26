package kz.hashiroii.domain.model

data class FinancialSummary(
    val totalIncome: Double,
    val totalExpenses: Double,
    val categoryBreakdown: Map<TransactionCategory, Double>
) {
    val difference: Double get() = totalIncome - totalExpenses
}