package com.kille.infrastructure.persistence.adapter

import com.kille.domain.exception.EntityNotFoundException
import com.kille.domain.model.BookId
import com.kille.domain.model.Bookmark
import com.kille.domain.model.BookmarkId
import com.kille.domain.model.BookmarkType
import com.kille.domain.model.ChapterId
import com.kille.domain.repository.BookmarkRepository
import com.kille.infrastructure.persistence.entity.BookmarkJpaEntity
import com.kille.infrastructure.persistence.repository.BookmarkJpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
class BookmarkRepositoryAdapter(
    private val jpaRepository: BookmarkJpaRepository
) : BookmarkRepository {

    override fun save(bookmark: Bookmark): Bookmark {
        val savedEntity = jpaRepository.save(bookmark.toEntity())
        return savedEntity.toDomain()
    }

    override fun findById(id: BookmarkId): Optional<Bookmark> {
        return jpaRepository.findById(id.value)
            .map { it.toDomain() }
    }

    override fun findAllByUserId(userId: UUID): List<Bookmark> {
        return jpaRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
            .map { it.toDomain() }
    }

    override fun deleteById(id: BookmarkId) {
        if (!jpaRepository.existsById(id.value)) {
            throw EntityNotFoundException("Bookmark", id.toString())
        }
        jpaRepository.deleteById(id.value)
    }

    override fun existsById(id: BookmarkId): Boolean {
        return jpaRepository.existsById(id.value)
    }

    override fun existsByUserIdAndChapterIdAndTypeAndPosition(
        userId: UUID,
        chapterId: ChapterId,
        type: BookmarkType,
        position: Long
    ): Boolean {
        return jpaRepository.existsByUserIdAndChapterIdAndTypeAndPosition(
            userId = userId,
            chapterId = chapterId.value,
            type = type,
            position = position
        )
    }
}

fun BookmarkJpaEntity.toDomain(): Bookmark {
    return Bookmark.restore(
        id = BookmarkId(id),
        userId = userId,
        bookId = BookId(bookId),
        chapterId = ChapterId(chapterId),
        type = type,
        position = position,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Bookmark.toEntity(): BookmarkJpaEntity {
    return BookmarkJpaEntity(
        id = id.value,
        userId = userId,
        bookId = bookId.value,
        chapterId = chapterId.value,
        type = type,
        position = position,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
