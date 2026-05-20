package kz.hashiroii.data.repository

import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.repository.TransactionRepository

class TransactionRepositoryImpl : TransactionRepository {
    override suspend fun getAll(): List<Transaction> {
        TODO("Room Later")
    }

    override suspend fun save(transactions: List<Transaction>) {
        TODO("Room Later")
    }

    override suspend fun clear() {
        TODO("Room Later")
    }
}