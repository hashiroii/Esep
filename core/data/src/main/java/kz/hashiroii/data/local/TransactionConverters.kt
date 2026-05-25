package kz.hashiroii.data.local

import androidx.room.TypeConverter
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
    fun toTransactionType(value: String): TransactionType = TransactionType.valueOf(value)
}