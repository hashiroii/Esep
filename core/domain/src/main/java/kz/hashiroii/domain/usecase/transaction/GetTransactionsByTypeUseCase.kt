package kz.hashiroii.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.model.TransactionType
import kz.hashiroii.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionsByTypeUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(
        type: TransactionType
    ): Flow<List<Transaction>> {
        return repository.getByType(type)
    }
}