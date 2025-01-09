package io.github.kez_lab.calendar

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

internal data class MonthData(
    private val firstDayOfMonth: LocalDate,
    private val inDays: Int,
    private val outDays: Int,
) {
    private val daysInMonth = CalendarUtils.getDaysInMonth(firstDayOfMonth)
    private val previousMonth = firstDayOfMonth.minus(1, DateTimeUnit.MONTH)
    private val nextMonth = firstDayOfMonth.plus(1, DateTimeUnit.MONTH)

    val calendarMonth = CalendarMonth(
        firstDayOfMonth,
        (0 until inDays + daysInMonth + outDays)
            .map { dayOffset -> getDay(dayOffset - inDays) }
            .chunked(7)
    )

    private fun getDay(dayOffset: Int): CalendarDay {
        val date = firstDayOfMonth.plus(dayOffset, DateTimeUnit.DAY)
        val position = when (date.month) {
            firstDayOfMonth.month -> DayPosition.IN_MONTH
            previousMonth.month -> DayPosition.OUTSIDE
            nextMonth.month -> DayPosition.OUTSIDE
            else -> throw IllegalArgumentException("Invalid date: $date in month: $firstDayOfMonth, dayOffset: $dayOffset")
        }
        return CalendarDay(date, position)
    }
}


internal fun getCalendarMonthData(
    startDate: LocalDate,
    offset: Long,
): MonthData {
    val targetMonth = startDate.plus(offset, DateTimeUnit.MONTH)
    val firstDayOfMonth = LocalDate(targetMonth.year, targetMonth.monthNumber, 1)

    val daysInMonth = CalendarUtils.getDaysInMonth(firstDayOfMonth)
    val inDays = firstDayOfMonth.dayOfWeek.ordinal
    val totalCells = inDays + daysInMonth

    val outDaysCells = (7 - totalCells % 7).takeIf { totalCells % 7 != 0 } ?: 0 // 다음 달 표시할 셀 개수

    return MonthData(
        firstDayOfMonth = firstDayOfMonth,
        inDays = inDays,
        outDays = outDaysCells
    )
}
