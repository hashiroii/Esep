package kz.hashiroii.domain.usecase

import kz.hashiroii.domain.model.Transaction
import javax.inject.Inject

class ParsePdfUseCase @Inject constructor(private val parser: PdfParser) {
    operator fun invoke(text: String): List<Transaction> = parser.parse(text)
}