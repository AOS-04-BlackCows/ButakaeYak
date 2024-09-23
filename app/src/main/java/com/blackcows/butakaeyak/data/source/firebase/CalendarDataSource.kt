package com.blackcows.butakaeyak.data.source.firebase

import com.blackcows.butakaeyak.data.models.Calendar
import com.blackcows.butakaeyak.data.toMap
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@Deprecated("캘린더가 굳이 필요한가??")
class CalendarDataSource @Inject constructor(

) {
    companion object {
        private const val TAG = "CalendarDataSource"
        private const val CALENDAR_COLLECTION = "calendars"
        private const val USER_ID = "userId"
        private const val YEAR_MONTH = "yearMonth"
    }
    private val db = Firebase.firestore

    //TODO: 캘린더 어케 저장되는지 확인해야함.

    suspend fun addCalendar(calendar: Calendar) {
        val result = db.collection(CALENDAR_COLLECTION)
            .add(calendar.toMap())
            .await()
    }
}