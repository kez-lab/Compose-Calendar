package io.github.kez_lab.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import io.github.kez_lab.calendar.ui.MonthCalendar
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent

    Box(
        modifier = Modifier
            .background(backgroundColor, shape = CircleShape)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

@Composable
internal fun DefaultCalendarHeader(calendarMonth: CalendarMonth) {
    Text(
        text = "📆 ${
            calendarMonth.localDate.month.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            )
        } ${calendarMonth.localDate.year}",
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
    )
}

@Preview
@Composable
fun CalendarPreview() {
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
