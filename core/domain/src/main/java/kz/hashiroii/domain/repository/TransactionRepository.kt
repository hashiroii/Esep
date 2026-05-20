package kz.hashiroii.domain.repository

import kz.hashiroii.domain.model.Transaction

interface TransactionRepository {
    suspend fun getAll(): List<Transaction>
    suspend fun save(transactions: List<Transaction>)
    suspend fun clear()
}