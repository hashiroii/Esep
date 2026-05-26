package kz.hashiroii.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kz.hashiroii.data.local.dto.TransactionDao
import kz.hashiroii.data.local.entity.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 3, exportSchema = true)
@TypeConverters(TransactionConverters::class)
abstract class EsepDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}