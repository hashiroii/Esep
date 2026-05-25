package kz.hashiroii.data.parser

import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.model.TransactionType
import kz.hashiroii.domain.usecase.PdfParser
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class KaspiStatementParser @Inject constructor() : PdfParser {

    private val pattern = Regex(
        """(\d{2}\.\d{2}\.\d{2})\s+([+\-])\s+([\d\s]+,\d{2})\s+₸\s+(\w+)\s{2,}(.+)"""
    )
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")

    override fun parse(text: String): List<Transaction> {
        val result = mutableListOf<Transaction>()
        val lines = text.lines()

        for (i in lines.indices) {
            val match = pattern.find(lines[i]) ?: continue

            val amount = match.groupValues[3]
                .replace(" ", "")
                .replace(",", ".")
                .toDouble()

            val type = when (match.groupValues[4].lowercase()) {
                "purchases" -> TransactionType.PURCHASES
                "transfers" -> TransactionType.TRANSFERS
                "replenishment" -> TransactionType.REPLENISHMENT
                "others" -> TransactionType.OTHERS
                else -> TransactionType.UNKNOWN
            }

            result.add(
                Transaction(
                    id = 0,
                    date = LocalDate.parse(match.groupValues[1], dateFormatter),
                    isIncome = match.groupValues[2] == "+",
                    amount = amount,
                    type = type,
                    merchant = match.groupValues[5].trim()
                )
            )
        }

        return result
    }
}