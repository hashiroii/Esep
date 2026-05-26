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
        if (isIncome
            || type == TransactionType.REPLENISHMENT
            || type == TransactionType.TRANSFERS
            || type == TransactionType.OTHERS
        ) return TransactionCategory.TRANSFERS

        val m = merchant.lowercase().trim()

        if (m == "cu") return TransactionCategory.GROCERIES

        return when {
            m.anyOf(
                // EN / transliteration
                "cafe", "coffee", "kfc", "mcdonald", "burger", "pizza", "sushi",
                "dodo", "restaurant", "grill", "doner", "shawarma", "bliny",
                "wedrink", "we drink", "big doner", "masterbliny",
                // RU Cyrillic
                "кафе", "ресторан", "столовая", "шаурма", "шашлык", "донер",
                "лагман", "чайхана", "чебуречн", "пельмен", "блин", "бар",
                // Known Kazakhstan food brands / venues
                "есахмет", "нурхан", "ширкин", "рауза", "тауекел"
            ) -> TransactionCategory.FOOD

            m.anyOf(
                // EN / transliteration (note: Cyrillic "маркет" ≠ ASCII "market" — both needed)
                "magnum", "ramstore", "spar", "sultan", "carrefour",
                "supermarket", "mini market", "minimarket",
                "produkyt", "prodykty", "produkty", "super cena",
                // RU Cyrillic — the critical ones missing before
                "супермаркет", "маркет", "рынок", "продукт", "мини маркет",
                // Known stores
                "дария", "юбилейный", "galmart", "kok dala", "galmart"
            ) -> TransactionCategory.GROCERIES

            m.anyOf(
                // EN
                "taxi", "uber", "bolt", "bus", "subway", "transport",
                "parking", "almaty-parking",
                // RU Cyrillic
                "такси", "автобус", "метро", "парковка", "паркинг",
                "автомойка", "мойка", "для авто",
                // Yandex transport products
                "yandex", "яндекс", "indrive","индрайв"
            ) -> TransactionCategory.TRANSPORT

            m.anyOf(
                // EN
                "petrol", "gas station", "fuel", "shell", "lukoil", "helios",
                "royal petrol",
                // RU Cyrillic
                "бензин", "азс", "заправ"
            ) -> TransactionCategory.FUEL

            m.anyOf(
                // EN
                "pharmacy", "clinic", "hospital", "health", "doctor", "dental", "medical",
                // RU Cyrillic
                "аптека", "клиника", "больниц", "медицин", "стоматол", "дента"
            ) -> TransactionCategory.HEALTH

            m.anyOf(
                // EN / streaming / subscriptions
                "youtube", "netflix", "spotify", "steam", "cinema", "movie",
                "billiard", "bowling", "karaoke", "dance studio", "skillz",
                "subscription", "claude.ai", "google",
                // RU Cyrillic
                "кино", "игр", "танц", "боулинг"
            ) -> TransactionCategory.ENTERTAINMENT

            m.anyOf(
                // Telecom / internet / utilities
                "kcell", "beeline", "activ", "tele2", "meganet", "internet",
                "communal", "electric", "utility",
                // RU Cyrillic
                "коммун", "квартплат", "интернет", "электр", "мобильн"
            ) -> TransactionCategory.UTILITIES

            m.anyOf(
                // EN
                "shop", "store", "zara", "wildberries", "lamoda",
                // RU Cyrillic — "маркет" is in GROCERIES above, but generic "магазин" here
                "магазин", "торгов", "одежд", "ювелир"
            ) -> TransactionCategory.SHOPPING

            else -> TransactionCategory.OTHER
        }
    }

    private fun String.anyOf(vararg keywords: String): Boolean = keywords.any { contains(it) }
}