package com.kille.domain.model

import com.kille.domain.exception.DomainException
import java.time.LocalDateTime
import java.util.UUID

class Bookmark private constructor(
    val id: BookmarkId,
    val userId: UUID,
    val bookId: BookId,
    val chapterId: ChapterId,
    val type: BookmarkType,
    val position: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {

    init {
        validatePosition(position)
    }

    companion object {
        fun create(
            userId: UUID,
            chapter: Chapter,
            type: BookmarkType,
            position: Long
        ): Bookmark {
            validateAgainstChapter(chapter, type, position)

            val now = LocalDateTime.now()
            return Bookmark(
                id = BookmarkId.generate(),
                userId = userId,
                bookId = chapter.bookId,
                chapterId = chapter.id,
                type = type,
                position = position,
                createdAt = now,
                updatedAt = now
            )
        }

        fun restore(
            id: BookmarkId,
            userId: UUID,
            bookId: BookId,
            chapterId: ChapterId,
            type: BookmarkType,
            position: Long,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime
        ): Bookmark {
            validatePosition(position)
            return Bookmark(
                id = id,
                userId = userId,
                bookId = bookId,
                chapterId = chapterId,
                type = type,
                position = position,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }

        private fun validateAgainstChapter(
            chapter: Chapter,
            type: BookmarkType,
            position: Long
        ) {
            validatePosition(position)

            when (type) {
                BookmarkType.READING -> {
                    if (!chapter.hasText()) {
                        throw DomainException("Reading bookmark requires chapter text")
                    }

                    val textLength = chapter.text?.length?.toLong()
                    if (textLength != null && position > textLength) {
                        throw DomainException("Bookmark position exceeds chapter text length")
                    }
                }

                BookmarkType.LISTENING -> {
                    if (!chapter.hasAudio()) {
                        throw DomainException("Listening bookmark requires chapter audio")
                    }

                    val duration = chapter.durationMs
                    if (duration != null && position > duration) {
                        throw DomainException("Bookmark position exceeds chapter duration")
                    }
                }
            }
        }

        private fun validatePosition(position: Long) {
            if (position < 0) {
                throw DomainException("Bookmark position must be greater than or equal to 0")
            }
        }
    }
}
