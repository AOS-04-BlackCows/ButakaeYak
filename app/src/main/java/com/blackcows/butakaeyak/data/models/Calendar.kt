package com.blackcows.butakaeyak.data.models

import com.google.gson.annotations.Expose
import io.ktor.util.date.WeekDay
import java.time.LocalDate

data class Calendar(
    @Expose(serialize = false)
    val id: String,
    val userId: String,
    val yearMonth: String,      //format: "20240912"
    val diaries: List<Diary>       //format: "24:내용"
) {
    data class Diary(
        val date: LocalDate,
        val content: String
    )
}
