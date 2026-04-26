package com.kille.domain.repository

import com.kille.domain.model.Bookmark
import com.kille.domain.model.BookmarkId
import com.kille.domain.model.BookmarkType
import com.kille.domain.model.ChapterId
import java.util.Optional
import java.util.UUID

interface BookmarkRepository {
    fun save(bookmark: Bookmark): Bookmark
    fun findById(id: BookmarkId): Optional<Bookmark>
    fun findAllByUserId(userId: UUID): List<Bookmark>
    fun deleteById(id: BookmarkId)
    fun existsById(id: BookmarkId): Boolean
    fun existsByUserIdAndChapterIdAndTypeAndPosition(
        userId: UUID,
        chapterId: ChapterId,
        type: BookmarkType,
        position: Long
    ): Boolean
}
