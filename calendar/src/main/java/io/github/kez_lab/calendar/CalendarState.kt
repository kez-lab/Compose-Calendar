package io.github.kez_lab.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

private const val PAGE_COUNT = 1200

@OptIn(ExperimentalFoundationApi::class)
@Stable
class CalendarState(
    val pagerState: PagerState,
    private val initialDate: LocalDate
) {
    val currentDate: LocalDate
        get() = initialDate.plus(pagerState.currentPage - PAGE_COUNT / 2, DateTimeUnit.MONTH)

    var selectedDate by mutableStateOf<LocalDate?>(null)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberCalendarState(
    initialDate: LocalDate = remember { currentDate }
): CalendarState {
    val pagerState = rememberPagerState(
        initialPage = PAGE_COUNT / 2,
        pageCount = { PAGE_COUNT }
    )
    return remember { CalendarState(pagerState, initialDate) }
}