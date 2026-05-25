package kz.hashiroii.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.repository.TransactionRepository
import java.time.LocalDate
import javax.inject.Inject

class GetTransactionsByPeriodUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(
        start: LocalDate,
        end: LocalDate
    ): Flow<List<Transaction>> {
        return repository.getByPeriod(start, end)
    }
}