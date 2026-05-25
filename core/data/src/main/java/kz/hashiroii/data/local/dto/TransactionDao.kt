package kz.hashiroii.data.local.dto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kz.hashiroii.data.local.entity.TransactionEntity
import kz.hashiroii.domain.model.TransactionType
import java.time.LocalDate

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = :type") // for search
    fun getTransactionsByType(type: TransactionType): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionsById(id: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE date BETWEEN :start AND :end")
    fun getTransactionsByPeriod(start: LocalDate, end: LocalDate): Flow<List<TransactionEntity>>

    @Insert
    suspend fun saveTransactions(transactions: List<TransactionEntity>): List<Long>

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)
}