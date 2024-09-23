package com.blackcows.butakaeyak.data.models

import com.google.gson.annotations.Expose
import java.time.LocalDate

data class Memo(
    @Expose(serialize = false)
    val id: String,
    val userId: String,
    val group: MedicineGroup,
    val content: String,
    val createdAt: LocalDate,
    val updatedAt: LocalDate
) {
    fun toRequest()
        = MemoRequest(
            userId, group.id, content, createdAt.toString(), updatedAt.toString()
        )
}

data class MemoRequest(
    val userId: String,
    val groupId: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String
)
data class MemoResponse(
    val id: String,
    val userId: String,
    val groupId: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toMemo(group: MedicineGroup): Memo {
        return Memo(
            id = this.id,
            userId = this.userId,
            group = group,
            content = this.content,
            createdAt = LocalDate.parse(this.createdAt),
            updatedAt = LocalDate.parse(this.updatedAt)
        )
    }

    fun toRequest() = MemoRequest(
        userId = userId,
        groupId = groupId,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
