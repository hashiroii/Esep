package kz.hashiroii.domain.usecase.transaction

import kz.hashiroii.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionByIdUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteById(id)
    }
}