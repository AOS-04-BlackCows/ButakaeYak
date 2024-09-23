package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.Memo
import java.time.LocalDate

interface MemoRepository {
    suspend fun getMemosByUserId(userId: String): List<Memo>
    suspend fun getMemoByMedicineGroupId(groupId: String): List<Memo>
    suspend fun getMemosFromWhen(userId: String, createdAt: LocalDate): List<Memo>

    suspend fun createMemo(memo: Memo)
    suspend fun editMemo(memo: Memo, content: String)

    suspend fun deleteMemo(memo: Memo)

}