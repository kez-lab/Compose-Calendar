package io.github.kez_lab.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import java.time.format.TextStyle
import java.util.Locale

/**
 * 📅 Jetpack Compose Calendar Component
 *
 * @param modifier The [Modifier] to be applied to the calendar
 * @param calendarState The state object that manages the calendar
 * @param headerContent A composable slot for customizing the calendar header
 * @param dayContent A composable slot for customizing day cells
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    calendarState: CalendarState = rememberCalendarState(),
    headerContent: @Composable (LocalDate) -> Unit = { date ->
        DefaultCalendarHeader(date)
    },
    dayContent: @Composable CalendarScope.(LocalDate) -> Unit = { date ->
        DefaultCalendarDay(date, calendarState)
    }
) {
    Surface(modifier = modifier) {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = calendarState.pagerState,
        ) { page ->
            val displayedDate = remember(page) {
                calendarState.initialDate.plus(page - PAGE_COUNT / 2, DateTimeUnit.MONTH)
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                headerContent(displayedDate)
                MonthCalendar(
                    year = displayedDate.year,
                    month = displayedDate.monthNumber,
                    selectedDate = calendarState.selectedDate,
                    onDateSelected = { calendarState.selectedDate = it },
                    dayContent = dayContent
                )
            }
        }
    }
}

/**
 * 📆 Displays a monthly calendar for the given year and month
 *
 * @param year The year to display
 * @param month The month to display
 * @param selectedDate The currently selected date
 * @param onDateSelected Callback triggered when a date is selected
 * @param dayContent A composable slot for customizing day cells
 */
@Composable
fun MonthCalendar(
    year: Int,
    month: Int,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    dayContent: @Composable CalendarScope.(LocalDate) -> Unit
) {
    val firstDayOfMonth = LocalDate(year, month, 1)
    val daysInMonth = CalendarUtils.getDaysInMonth(firstDayOfMonth)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.ordinal
    val totalCells = firstDayOfWeek + daysInMonth
    val emptyCells = (7 - totalCells % 7).takeIf { totalCells % 7 != 0 } ?: 0

    LazyVerticalGrid(
        modifier = Modifier.height(GRID_HEIGHT),
        columns = GridCells.Fixed(7)
    ) {
        items(DayOfWeek.entries, key = { "dayOfWeek-${it.ordinal}" }) { dayOfWeek ->
            Text(
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                textAlign = TextAlign.Center,
                modifier = Modifier.size(CELL_SIZE)
            )
        }

        items(firstDayOfWeek, key = { "empty-start-$it" }) {
            Spacer(Modifier.size(CELL_SIZE))
        }

        items(daysInMonth, key = { "day-${it + 1}" }) { day ->
            val date = LocalDate(year, month, day + 1)
            CalendarScope(selectedDate, onDateSelected).dayContent(date)
        }

        items(emptyCells, key = { "empty-end-$it" }) {
            Spacer(Modifier.size(CELL_SIZE))
        }
    }
}

/**
 * 🔹 Represents a single day cell in the calendar
 *
 * @param dayNumber The day number (1-31)
 * @param isSelected Whether the day is currently selected
 * @param onClick Callback triggered when the cell is clicked
 */
@Composable
fun DayCell(
    dayNumber: Int,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent

    Box(
        modifier = Modifier
            .size(CELL_SIZE)
            .background(backgroundColor, shape = CircleShape)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = dayNumber.toString(), color = if (isSelected) Color.White else Color.Black)
    }
}

@Composable
internal fun DefaultCalendarHeader(date: LocalDate) {
    Text(
        text = "📆 ${date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${date.year}",
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
internal fun DefaultCalendarDay(
    date: LocalDate,
    calendarState: CalendarState
) {
    DayCell(
        dayNumber = date.dayOfMonth,
        isSelected = date == calendarState.selectedDate,
        onClick = { calendarState.selectedDate = date }
    )
}

@Preview
@Composable
fun CalendarPreview() {
    Calendar()
}