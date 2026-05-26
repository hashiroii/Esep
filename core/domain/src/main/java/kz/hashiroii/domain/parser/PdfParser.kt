package kz.hashiroii.domain.parser

import kz.hashiroii.domain.model.Transaction

interface PdfParser {
    fun parse(text: String): List<Transaction>
}