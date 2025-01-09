package io.github.kez_lab.calendar.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import io.github.kez_lab.calendar.CalendarDay
import io.github.kez_lab.calendar.CalendarMonth
import io.github.kez_lab.calendar.Day
import io.github.kez_lab.calendar.DayPosition
import io.github.kez_lab.calendar.DefaultCalendarHeader
import io.github.kez_lab.calendar.GRID_HEIGHT
import io.github.kez_lab.calendar.OutDateStyle
import io.github.kez_lab.calendar.currentDate
import io.github.kez_lab.calendar.getCalendarMonthData
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MonthCalendar(
    calendarMonth: CalendarMonth,
    monthHeader: @Composable ((CalendarMonth) -> Unit)?,
    monthFooter: @Composable ((CalendarMonth) -> Unit)?,
    dayContent: @Composable ((CalendarDay) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        monthHeader?.invoke(calendarMonth)

        LazyVerticalGrid(
            modifier = Modifier.height(GRID_HEIGHT),
            columns = GridCells.Fixed(7)
        ) {
            items(DayOfWeek.entries, key = { "dayOfWeek-${it.ordinal}" }) { dayOfWeek ->
                Box(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            val daysInMonth = calendarMonth.weekDays.flatten()
            items(daysInMonth, key = { "day-${it.date}" }) { day ->
                Box(modifier = Modifier.weight(1f)) {
                    dayContent?.invoke(day)
                }
            }
        }

        monthFooter?.invoke(calendarMonth)
    }
}

@Composable
fun SampleCalendar(modifier: Modifier = Modifier) {
    val startDate = remember { currentDate }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val firstDayOfWeek = DayOfWeek.SUNDAY

    val calendarMonth = remember(startDate, firstDayOfWeek) {
        getCalendarMonthData(startDate, 0).calendarMonth
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        MonthCalendar(
            calendarMonth = calendarMonth,
            monthHeader = { DefaultCalendarHeader(it) },
            monthFooter = null,
            dayContent = { day ->
                Day(day) {
                    selectedDate = day.date
                }
            }
        )
        Day(
            day = CalendarDay(LocalDate(2022, 1, 1), DayPosition.IN_MONTH),
            isSelected = false
        )
    }
}