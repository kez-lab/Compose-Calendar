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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import java.time.format.TextStyle
import java.util.Locale

private val CELL_SIZE = 30.dp
private val GRID_HEIGHT = 260.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    calendarState: CalendarState = rememberCalendarState()
) {
    Surface(modifier = modifier) {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = calendarState.pagerState,
            beyondBoundsPageCount = 1,
        ) {
            val displayedDate = calendarState.currentDate

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${displayedDate.year}년 ${displayedDate.monthNumber}월",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineMedium
                )
                MonthCalendar(
                    year = displayedDate.year,
                    month = displayedDate.monthNumber,
                    selectedDate = calendarState.selectedDate,
                    onDateSelected = { calendarState.selectedDate = it }
                )
            }
        }
    }
}

@Composable
fun MonthCalendar(
    year: Int,
    month: Int,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = remember(year, month) { LocalDate(year, month, 1) }
    val daysInMonth = remember(firstDayOfMonth) { CalendarUtils.getDaysInMonth(firstDayOfMonth) }
    val firstDayOfWeek = remember(firstDayOfMonth) { firstDayOfMonth.dayOfWeek.ordinal }
    val totalCells = firstDayOfWeek + daysInMonth
    val emptyCells = remember(totalCells) {
        (7 - totalCells % 7).takeIf { totalCells % 7 != 0 } ?: 0
    }

    LazyVerticalGrid(
        modifier = Modifier.height(GRID_HEIGHT),
        columns = GridCells.Fixed(7)
    ) {
        items(DayOfWeek.entries, key = { "dayOfWeek-${it.ordinal}" }) { dayOfWeek ->
            DayCell(
                dayLabel = remember(dayOfWeek) {
                    dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    )
                }
            )
        }

        items(firstDayOfWeek, key = { "empty-start-$it" }) {
            Spacer(Modifier.size(CELL_SIZE))
        }

        items(daysInMonth, key = { "day-${it + 1}" }) { day ->
            val date = remember { LocalDate(year, month, day + 1) }
            DayCell(
                dayNumber = day + 1,
                isSelected = date == selectedDate,
                onClick = { onDateSelected(date) }
            )
        }

        items(emptyCells, key = { "empty-end-$it" }) {
            Spacer(Modifier.size(CELL_SIZE))
        }
    }
}

@Composable
fun DayCell(
    dayNumber: Int? = null,
    dayLabel: String? = null,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val text = remember(dayNumber, dayLabel) { dayLabel ?: dayNumber.toString() }
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent

    Box(
        modifier = Modifier
            .size(CELL_SIZE)
            .background(backgroundColor, shape = ShapeDefaults.Medium)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = if (isSelected) Color.White else Color.Black)
    }
}

@Preview
@Composable
fun CalendarPreview() {
    Calendar()
}