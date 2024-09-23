package com.blackcows.butakaeyak.ui.schedule

import com.blackcows.butakaeyak.data.models.MedicineGroup

data class TimeToGroup(
    val alarm: String,
    val groups: List<MedicineGroup>
)