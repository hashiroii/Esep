package kz.hashiroii.domain.usecase

import kz.hashiroii.domain.model.Transaction

class ParsePdfUseCase(private val parser: PdfParser) {
    operator fun invoke(text: String): List<Transaction> {
        return parser.parse(text)
    }
}

interface PdfParser {
    fun parse(text: String): List<Transaction>
}