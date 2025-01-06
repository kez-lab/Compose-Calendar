package io.github.kez_lab.calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kez_lab.calendar.ui.theme.CalendarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalendarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalendarApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CalendarApp(modifier: Modifier = Modifier) {
    val calendarState = rememberCalendarState()

    Column(modifier = modifier) {
        Text("현재 페이지 날짜: ${calendarState.currentDate}")
        Text("선택한 날짜: ${calendarState.selectedDate ?: "선택 없음"}")

        Calendar(
            calendarState = calendarState,
            headerContent = { date ->
                Text(
                    text = "📅 ${date.year}년 ${date.monthNumber}월",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )
            },
            dayContent = { date ->
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(if (date == selectedDate) Color.Red else Color.Transparent)
                        .clickable { onDateSelected(date) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = date.dayOfMonth.toString(), color = Color.Blue)
                }
            }
        )
    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalendarTheme {
        CalendarApp()
    }
}