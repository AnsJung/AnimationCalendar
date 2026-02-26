package com.example.animationcalendar

import android.R.attr.data
import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animationcalendar.ui.CalendarData.spendingData
import com.example.animationcalendar.ui.theme.AnimationCalendarTheme
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimationCalendarTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .background(Color.White)
                ) {
                    CalendarHeader()
                    CalendarDayLazyList()
                }
            }
        }
    }
}

@Composable
fun CalendarHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.End,
    ) {

        FaIcon(
            faIcon = FaIcons.Cog,
            size = 30.dp,
            tint = Color.Gray,
            modifier = Modifier.padding(end = 10.dp)
        )

        FaIcon(
            faIcon = FaIcons.Bell,
            size = 30.dp,
            tint = Color.Gray,
            modifier = Modifier.padding(end = 10.dp)
        )

        FaIcon(
            faIcon = FaIcons.Bars,
            size = 30.dp,
            tint = Color.Gray,
            modifier = Modifier.padding(end = 10.dp)
        )
    }
}

@Composable
fun CalendarDayNames() {
    val nameList = listOf("일", "월", "화", "수", "목", "금", "토")
    Row() {
        nameList.forEach { name ->
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name,
                    fontSize = 14.sp,
                )
            }
        }
    }
}

@Composable
fun CalendarDayList() {
    val time = remember { mutableStateOf(Calendar.getInstance()) }
    val date = time.value
    date.set(Calendar.YEAR, 2026)
    date.set(Calendar.MONTH, Calendar.JANUARY)
    date.set(Calendar.DAY_OF_MONTH, 1)

    // ?
    // 달력계산 공식
    val thisMonthDayMax = date.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = date.get(Calendar.DAY_OF_WEEK) - 1
    val thisMothWeeksCount = (thisMonthDayMax + firstDayOfWeek + 6) / 7 // 현재 달의 week 수 계산 공식

    Column(modifier = Modifier.padding(top = 20.dp)) {
        repeat(thisMothWeeksCount) { weekIndex ->
            Row() {
                repeat(7) { dayIndex ->
                    val day = weekIndex * 7 + dayIndex - firstDayOfWeek + 1
                    if (day in 1..thisMonthDayMax) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .border(1.dp, Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(20.dp)
                                        .background(Color(0xFF89CFF0))
                                ) {

                                }
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = day.toString(),
                                        fontSize = 14.sp,
                                    )
                                }

                            }

                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .border(1.dp, Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(20.dp)
                                        .background(Color(0xFF89CFF0))
                                ) {

                                }

                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun CalendarDayLazyList() {
    val lazyColumnState = rememberLazyListState()
    val lazyRowState = rememberLazyListState()
    val isScrolling = remember { mutableStateOf(false) } // by ?

    LaunchedEffect(Unit) {
        // ?
        snapshotFlow { lazyColumnState.isScrollInProgress }.collect {
            isScrolling.value = it
        }
    }
    // ?
    LaunchedEffect(lazyColumnState.firstVisibleItemIndex) {
        lazyRowState.scrollToItem(lazyRowState.firstVisibleItemIndex)
    }

    if (isScrolling.value) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            state = lazyRowState
        ) {
            items(spendingData.size) {
                val day = it + 1
                Box(
                    modifier = Modifier
                        .width(55.dp)
                        .height(60.dp)
                        .border(1.dp, Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                                .background(Color(0xFF89CFF0))
                        ) {

                        }
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.toString(),
                                fontSize = 14.sp,
                            )
                        }

                    }

                }
            }
        }
    } else {
        CalendarDayNames()
        CalendarDayList()
    }
    LazyColumn(modifier = Modifier.padding(20.dp), state = lazyColumnState) {
        // key?
        spendingData.keys.forEach { day ->
            item {
                Text(
                    "2026년 1월 ${day}일",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                spendingData[day]?.forEach { data ->
                    Row(
                        modifier = Modifier
                            .padding(start = 5.dp, top = 3.dp),
                    ) {
                        Text(data.type, fontSize = 12.sp)
                        Text(
                            "${data.price}원",
                            fontSize = 12.sp,
                            color = if (data.type == "수입") Color.Red else Color.Blue,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
            }
        }
    }
}