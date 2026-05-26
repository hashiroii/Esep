package kz.hashiroii.data.local

import androidx.room.TypeConverter
import kz.hashiroii.domain.model.TransactionCategory
import kz.hashiroii.domain.model.TransactionType
import java.time.LocalDate

class TransactionConverters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.toString()

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType =
        runCatching { TransactionType.valueOf(value) }.getOrDefault(TransactionType.UNKNOWN)

    @TypeConverter
    fun fromTransactionCategory(category: TransactionCategory): String = category.name

    @TypeConverter
    fun toTransactionCategory(value: String): TransactionCategory =
        runCatching { TransactionCategory.valueOf(value) }.getOrDefault(TransactionCategory.OTHER)
}