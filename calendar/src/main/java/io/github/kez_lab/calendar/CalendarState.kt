package io.github.kez_lab.calendar

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
data class CalendarMonth(
    val localDate: LocalDate,
    val weekDays: List<List<CalendarDay>>,
)

@Immutable
data class CalendarDay(val date: LocalDate, val position: DayPosition)

enum class DayPosition { IN_MONTH, OUTSIDE }
/**
 * Determines how [DayPosition.OutDate] are
 * generated for each month on the calendar.
 */
public enum class OutDateStyle {
    /**
     * The calendar will generate outDates until it reaches
     * the end of the month row. This means that if a month
     * has 5 rows, it will display 5 rows and if a month
     * has 6 rows, it will display 6 rows.
     */
    EndOfRow,

    /**
     * The calendar will generate outDates until it
     * reaches the end of a 6 x 7 grid on each month.
     * This means that all months will have 6 rows.
     */
    EndOfGrid,
}
