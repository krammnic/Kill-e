package com.kille.presentation.dto.response

import com.kille.domain.model.Bookmark
import com.kille.domain.model.BookmarkType
import java.time.LocalDateTime
import java.util.UUID

data class BookmarkResponse(
    val id: UUID,
    val userId: UUID,
    val bookId: UUID,
    val chapterId: UUID,
    val type: BookmarkType,
    val position: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromDomain(bookmark: Bookmark): BookmarkResponse {
            return BookmarkResponse(
                id = bookmark.id.value,
                userId = bookmark.userId,
                bookId = bookmark.bookId.value,
                chapterId = bookmark.chapterId.value,
                type = bookmark.type,
                position = bookmark.position,
                createdAt = bookmark.createdAt,
                updatedAt = bookmark.updatedAt
            )
        }
    }
}
