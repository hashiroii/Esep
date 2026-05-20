package kz.hashiroii.data.parser

import kz.hashiroii.domain.model.Transaction
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

        // block case
        val nextLine = if (i + 1 < lines.size) lines[i + 1] else ""
        val nextNextLine = if (i + 2 < lines.size) lines[i + 2] else ""


        val date = match.groupValues[1]
        val sign = match.groupValues[2]
        val amount = match.groupValues[3]
            .replace(" ", "")
            .replace(",", ".")
            .toDouble()
        val type = match.groupValues[4]
        val merchant = match.groupValues[5].trim()
        val isBlocked = nextLine.contains("blocked", ignoreCase = true)
                || nextNextLine.contains("blocked", ignoreCase = true)

        result.add(
            Transaction(
                date = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yy")),
                isIncome = sign == "+",
                amount = amount,
                type = type,
                merchant = merchant,
                isBlocked = isBlocked
            )
        )
    }

    return result
}
