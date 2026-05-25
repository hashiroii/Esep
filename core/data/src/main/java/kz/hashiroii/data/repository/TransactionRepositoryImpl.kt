package kz.hashiroii.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kz.hashiroii.data.local.dto.TransactionDao
import kz.hashiroii.data.local.entity.toDomain
import kz.hashiroii.data.local.entity.toEntity
import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.model.TransactionType
import kz.hashiroii.domain.repository.TransactionRepository
import java.time.LocalDate
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {

    override fun getAll(): Flow<List<Transaction>> {
        return dao.getAllTransactions().map { transactionEntities ->
            transactionEntities.map { transactionEntity ->
                transactionEntity.toDomain()
            }
        }
    }

    override fun getById(id: Long): Flow<List<Transaction>> {
        return dao.getTransactionsById(id).map { transactionEntities ->
            transactionEntities.map { transactionEntity ->
                transactionEntity.toDomain()
            }
        }
    }

    override fun getByType(type: TransactionType): Flow<List<Transaction>> {
        return dao.getTransactionsByType(type).map { transactionEntities ->
            transactionEntities.map { transactionEntity ->
                transactionEntity.toDomain()
            }
        }
    }

    override fun getByPeriod(
        start: LocalDate,
        end: LocalDate
    ): Flow<List<Transaction>> {
        return dao.getTransactionsByPeriod(start = start, end = end).map { transactionEntities ->
            transactionEntities.map { transactionEntity ->
                transactionEntity.toDomain()
            }
        }
    }

    override suspend fun save(transactions: List<Transaction>) {
        dao.saveTransactions(
            transactions.map { transaction ->
                transaction.toEntity()
            }
        )
    }

    override suspend fun deleteById(id: Long) {
        dao.deleteTransactionById(id)
    }
}