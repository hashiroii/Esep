package kz.hashiroii.domain.usecase.transaction

import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.repository.TransactionRepository
import javax.inject.Inject

class SaveTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transactions: List<Transaction>) {
        repository.save(transactions)
    }
}