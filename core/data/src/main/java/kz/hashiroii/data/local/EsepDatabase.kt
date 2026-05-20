package kz.hashiroii.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import kz.hashiroii.data.local.dto.TransactionDao
import kz.hashiroii.data.local.entity.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1, exportSchema = true)
abstract class EsepDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}