package com.blackcows.butakaeyak.data.models

import com.blackcows.butakaeyak.data.toKorean
import com.google.gson.annotations.Expose
import io.ktor.util.date.WeekDay
import java.time.LocalDate

data class MedicineGroup (
    @Expose(serialize = false)
    val id: String,
    val name: String,
    val userId: String,
    val medicines: List<MedicineDetail>,
    val customNameList: List<String>,
    val imageUrlList: List<String>,
    val startedAt: LocalDate,
    val finishedAt: LocalDate,
    val daysOfWeeks: List<WeekDay>,
    val alarms: List<String>,        //format: "10:30", "12:40"
    val hasTaken: List<String>            //format: "2024-09-12 10:30", "2024-09-12 10:30"
) {
    fun toRequest() = MedicineGroupRequest(
        name = name,
        userId = userId,
        medicineIdList = medicines.map { it.id },
        customNameList = customNameList,
        imageUrlList = imageUrlList,
        startedAt = startedAt.toString(),
        finishedAt = finishedAt.toString(),
        daysOfWeeks = daysOfWeeks.map { it.toKorean() },
        alarms = alarms,
        hasTaken = hasTaken
    )
    fun toResponse() = MedicineGroupResponse(
        id = id,
        name = name,
        userId = userId,
        medicineIdList = medicines.map { it.id },
        customNameList = customNameList,
        imageUrlList = imageUrlList,
        startedAt = startedAt.toString(),
        finishedAt = finishedAt.toString(),
        daysOfWeeks = daysOfWeeks.map { it.toKorean() },
        alarms = alarms,
        hasTaken = hasTaken
    )
}

data class MedicineGroupRequest(
    val name: String,
    val userId: String,
    val medicineIdList: List<String>,
    val customNameList: List<String>,
    val imageUrlList: List<String>,
    val startedAt: String,
    val finishedAt: String,
    val daysOfWeeks: List<String>,
    val alarms: List<String>,
    val hasTaken: List<String>
)

data class MedicineGroupResponse(
    val id: String? = null,
    val name: String? = null,
    val userId: String? = null,
    val medicineIdList: List<String>? = null,
    val customNameList: List<String>? = null,
    val imageUrlList: List<String>? = null,
    val startedAt: String? = null,
    val finishedAt: String? = null,
    val daysOfWeeks: List<String>? = null,
    val alarms: List<String>? = null,
    val hasTaken: List<String>? = null
)
