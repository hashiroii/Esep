package kz.hashiroii.data.parser

import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.model.TransactionType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun parseTransaction(text: String): List<Transaction> {
    val pattern = Regex(
        """(\d{2}\.\d{2}\.\d{2})\s+([+\-])\s+([\d\s]+,\d{2})\s+₸\s+(\w+)\s{2,}(.+)"""
    )

    val result = mutableListOf<Transaction>()

    val lines = text.lines()
    for (i in lines.indices) {
        val match = pattern.find(lines[i]) ?: continue


        val date = match.groupValues[1]
        val sign = match.groupValues[2]
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
        val merchant = match.groupValues[5].trim()

        result.add(
            Transaction(
                id = 0,
                date = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yy")),
                isIncome = sign == "+",
                amount = amount,
                type = type,
                merchant = merchant,
            )
        )
    }

    return result
}
