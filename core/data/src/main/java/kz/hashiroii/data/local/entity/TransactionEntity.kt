package kz.hashiroii.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.model.TransactionCategory
import kz.hashiroii.domain.model.TransactionType
import java.time.LocalDate

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    val amount: Double,
    val isIncome: Boolean,
    val type: TransactionType,
    val merchant: String,
    val category: TransactionCategory = TransactionCategory.OTHER,
)

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        date = date,
        amount = amount,
        isIncome = isIncome,
        type = type,
        merchant = merchant,
        category = category
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        date = date,
        amount = amount,
        isIncome = isIncome,
        type = type,
        merchant = merchant,
        category = category
    )
}