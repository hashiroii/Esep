package kz.hashiroii.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.repository.TransactionRepository
import javax.inject.Inject

class GetAllTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<List<Transaction>> {
        return repository.getAll()
    }
}