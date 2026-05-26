package kz.hashiroii.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

enum class PeriodType(val displayName: String) {
    DAY("Day"),
    WEEK("Week"),
    MONTH("Month"),
    YEAR("Year"),
    CUSTOM("Custom")
}

data class Period(
    val type: PeriodType,
    val start: LocalDate,
    val end: LocalDate
) {
    val label: String
        get() {
            val en = Locale.ENGLISH
            return when (type) {
                PeriodType.DAY -> start.format(DateTimeFormatter.ofPattern("d MMM yyyy", en))
                PeriodType.WEEK -> {
                    val s = start.format(DateTimeFormatter.ofPattern("d MMM", en))
                    val e = end.format(DateTimeFormatter.ofPattern("d MMM yyyy", en))
                    "$s–$e"
                }
                PeriodType.MONTH -> start.format(DateTimeFormatter.ofPattern("MMMM yyyy", en))
                PeriodType.YEAR -> start.year.toString()
                PeriodType.CUSTOM -> {
                    val s = start.format(DateTimeFormatter.ofPattern("d MMM", en))
                    val e = end.format(DateTimeFormatter.ofPattern("d MMM yyyy", en))
                    "$s–$e"
                }
            }
        }

    fun next(): Period = when (type) {
        PeriodType.DAY -> Period(type, start.plusDays(1), start.plusDays(1))
        PeriodType.WEEK -> Period(type, start.plusWeeks(1), end.plusWeeks(1))
        PeriodType.MONTH -> {
            val s = start.plusMonths(1).withDayOfMonth(1)
            Period(type, s, s.with(TemporalAdjusters.lastDayOfMonth()))
        }
        PeriodType.YEAR -> {
            val y = start.year + 1
            Period(type, LocalDate.of(y, 1, 1), LocalDate.of(y, 12, 31))
        }
        PeriodType.CUSTOM -> this
    }

    fun previous(): Period = when (type) {
        PeriodType.DAY -> Period(type, start.minusDays(1), start.minusDays(1))
        PeriodType.WEEK -> Period(type, start.minusWeeks(1), end.minusWeeks(1))
        PeriodType.MONTH -> {
            val s = start.minusMonths(1).withDayOfMonth(1)
            Period(type, s, s.with(TemporalAdjusters.lastDayOfMonth()))
        }
        PeriodType.YEAR -> {
            val y = start.year - 1
            Period(type, LocalDate.of(y, 1, 1), LocalDate.of(y, 12, 31))
        }
        PeriodType.CUSTOM -> this
    }

    companion object {
        fun forType(type: PeriodType): Period {
            val today = LocalDate.now()
            return when (type) {
                PeriodType.DAY -> Period(type, today, today)
                PeriodType.WEEK -> {
                    val s = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    Period(type, s, s.plusDays(6))
                }
                PeriodType.MONTH -> {
                    val s = today.withDayOfMonth(1)
                    Period(type, s, s.with(TemporalAdjusters.lastDayOfMonth()))
                }
                PeriodType.YEAR -> Period(
                    type,
                    LocalDate.of(today.year, 1, 1),
                    LocalDate.of(today.year, 12, 31)
                )
                PeriodType.CUSTOM -> Period(type, today, today)
            }
        }
    }
}