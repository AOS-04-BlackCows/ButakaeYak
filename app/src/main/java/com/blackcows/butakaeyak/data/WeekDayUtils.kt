package com.blackcows.butakaeyak.data

import io.ktor.util.date.WeekDay
import io.ktor.util.date.WeekDay.FRIDAY
import io.ktor.util.date.WeekDay.MONDAY
import io.ktor.util.date.WeekDay.SATURDAY
import io.ktor.util.date.WeekDay.SUNDAY
import io.ktor.util.date.WeekDay.THURSDAY
import io.ktor.util.date.WeekDay.TUESDAY
import io.ktor.util.date.WeekDay.WEDNESDAY
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WeekDayUtils {
    fun fromKorean(str: String): WeekDay
            = when(str) {
        "월요일" -> MONDAY
        "화요일" -> TUESDAY
        "수요일" -> WEDNESDAY
        "목요일" -> THURSDAY
        "금요일" -> FRIDAY
        "토요일" -> SATURDAY
        "일요일" -> SUNDAY
        else -> throw Exception("Invalid day of week: $str")
    }
}



fun WeekDay.toKorean()
        = when(this) {
    MONDAY -> "월요일"
    TUESDAY -> "화요일"
    WEDNESDAY -> "수요일"
    THURSDAY -> "목요일"
    FRIDAY -> "금요일"
    SATURDAY -> "토요일"
    SUNDAY -> "일요일"
}

fun Date.toKorean(): String {
    val format = SimpleDateFormat("M월 dd일", Locale.getDefault())
    return format.format(this)
}