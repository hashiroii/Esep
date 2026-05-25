package kz.hashiroii.domain.usecase

import kz.hashiroii.domain.model.Transaction

interface PdfParser {
    fun parse(text: String): List<Transaction>
}