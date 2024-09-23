package com.blackcows.butakaeyak.ui.note.recycler

import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.Memo
import java.time.LocalDate

sealed class RecyclerItem {
    data class GroupMemos(
        val group: MedicineGroup,
        val memos: List<Memo>
    ): RecyclerItem()

    data class DateMemos(
        val date: LocalDate,
        val memos: List<Memo>
    ): RecyclerItem()
}