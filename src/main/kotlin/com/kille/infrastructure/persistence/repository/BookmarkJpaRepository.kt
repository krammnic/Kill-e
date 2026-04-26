package com.kille.infrastructure.persistence.repository

import com.kille.domain.model.BookmarkType
import com.kille.infrastructure.persistence.entity.BookmarkJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BookmarkJpaRepository : JpaRepository<BookmarkJpaEntity, UUID> {
    fun findAllByUserIdOrderByCreatedAtDesc(userId: UUID): List<BookmarkJpaEntity>
    fun existsByUserIdAndChapterIdAndTypeAndPosition(
        userId: UUID,
        chapterId: UUID,
        type: BookmarkType,
        position: Long
    ): Boolean
}
