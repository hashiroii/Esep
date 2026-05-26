package kz.hashiroii.data.parser

import kz.hashiroii.domain.model.Transaction
import kz.hashiroii.domain.model.TransactionCategory
import kz.hashiroii.domain.model.TransactionType
import kz.hashiroii.domain.parser.PdfParser
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

        for (line in text.lines()) {
            val match = pattern.find(line) ?: continue

            val amount = match.groupValues[3]
                .replace(" ", "")
                .replace(",", ".")
                .toDouble()

            val isIncome = match.groupValues[2] == "+"
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
                    date = LocalDate.parse(match.groupValues[1], dateFormatter),
                    isIncome = isIncome,
                    amount = amount,
                    type = type,
                    merchant = merchant,
                    category = categorize(merchant, type, isIncome)
                )
            )
        }

        return result
    }

    private fun categorize(merchant: String, type: TransactionType, isIncome: Boolean): TransactionCategory {
        if (isIncome || type == TransactionType.REPLENISHMENT || type == TransactionType.TRANSFERS) {
            return TransactionCategory.TRANSFERS
        }
        val m = merchant.lowercase()
        return when {
            m.anyOf("cafe", "coffee", "starbucks", "kfc", "mcdonald", "burger", "pizza", "sushi",
                "dodo", "чайхана", "restaurant", "ресторан", "bar ", "бар", "grill") -> TransactionCategory.FOOD

            m.anyOf("magnum", "small", "ramstore", "metro", "spar", "sultan", "carrefour",
                "globus", "supermarket", "продукт", "market") -> TransactionCategory.GROCERIES

            m.anyOf("taxi", "yandex", "uber", "bolt", "bus", "автобус", "metro ", "метро",
                "subway", "такси", "transport") -> TransactionCategory.TRANSPORT

            m.anyOf("petrol", "gas station", "азс", "helios", "lukoil", "shell", "бензин",
                "fuel", "заправ") -> TransactionCategory.FUEL

            m.anyOf("pharmacy", "аптека", "apteka", "clinic", "hospital", "медицина",
                "doctor", "health", "дента", "стоматол") -> TransactionCategory.HEALTH

            m.anyOf("cinema", "кино", "steam", "netflix", "spotify", "movie", "игр",
                "game", "playstation", "billiard", "боулинг", "karaoke") -> TransactionCategory.ENTERTAINMENT

            m.anyOf("kcell", "beeline", "activ", "tele2", "internet", "комунал",
                "communal", "electric", "коммун", "квартплат", "water ", "газ") -> TransactionCategory.UTILITIES

            m.anyOf("shop", "store", "zara", "h&m", "wildberries", "lamoda",
                "магазин", "торгов", "одежд", "ювелир") -> TransactionCategory.SHOPPING

            else -> TransactionCategory.OTHER
        }
    }

    private fun String.anyOf(vararg keywords: String): Boolean = keywords.any { contains(it) }
}