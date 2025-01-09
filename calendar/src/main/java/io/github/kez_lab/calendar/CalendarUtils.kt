package io.github.kez_lab.calendar

import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until

internal val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

internal val GRID_HEIGHT = 260.dp
internal val PAGE_COUNT = 1200

internal object CalendarUtils {
    fun getDaysInMonth(date: LocalDate): Int {
        return date.until(date.plus(1, DateTimeUnit.MONTH), DateTimeUnit.DAY)
    }
}