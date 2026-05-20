package kz.hashiroii.data.local.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.model.TransactionType

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    fun getTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE type = :type") // for search
    fun searchTransactionsByType(type: TransactionType): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun searchTransactionById(id: Long): Flow<List<Transaction>>

    @Insert
    suspend fun addTransaction(transaction: Transaction): Long

    @Delete
    suspend fun deleteTransactionById(id: Long)
}