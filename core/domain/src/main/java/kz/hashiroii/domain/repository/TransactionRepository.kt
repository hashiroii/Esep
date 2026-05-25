package kz.hashiroii.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.model.TransactionType
import java.time.LocalDate

interface TransactionRepository {
    fun getAll(): Flow<List<Transaction>>
    fun getById(id: Long): Flow<List<Transaction>>
    fun getByType(type: TransactionType): Flow<List<Transaction>>
    fun getByPeriod(start: LocalDate, end: LocalDate): Flow<List<Transaction>>
    suspend fun save(transactions: List<Transaction>)
    suspend fun deleteById(id: Long)
}