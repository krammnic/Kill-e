package com.kille.application.port.input.bookmark

import com.kille.application.port.input.UseCase
import com.kille.domain.exception.EntityNotFoundException
import com.kille.domain.exception.ValidationException
import com.kille.domain.model.Bookmark
import com.kille.domain.model.BookmarkType
import com.kille.domain.model.ChapterId
import com.kille.domain.repository.BookmarkRepository
import com.kille.domain.repository.ChapterRepository
import com.kille.presentation.dto.response.BookmarkResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CreateBookmarkUseCase(
    private val bookmarkRepository: BookmarkRepository,
    private val chapterRepository: ChapterRepository
) : UseCase<CreateBookmarkCommand, BookmarkResponse> {

    @Transactional
    override fun execute(input: CreateBookmarkCommand): BookmarkResponse {
        val userId = parseUserId(input.userId)
        val chapterId = ChapterId.fromString(input.chapterId)
        val chapter = chapterRepository.findById(chapterId)
            .orElseThrow { EntityNotFoundException("Chapter", input.chapterId) }

        if (bookmarkRepository.existsByUserIdAndChapterIdAndTypeAndPosition(
                userId = userId,
                chapterId = chapter.id,
                type = input.type,
                position = input.position
            )
        ) {
            throw ValidationException("Bookmark already exists for this user and chapter position")
        }

        val bookmark = Bookmark.create(
            userId = userId,
            chapter = chapter,
            type = input.type,
            position = input.position
        )

        return BookmarkResponse.fromDomain(bookmarkRepository.save(bookmark))
    }

    private fun parseUserId(value: String): UUID {
        return try {
            UUID.fromString(value)
        } catch (e: IllegalArgumentException) {
            throw ValidationException("Invalid userId format: $value")
        }
    }
}

data class CreateBookmarkCommand(
    val userId: String,
    val chapterId: String,
    val type: BookmarkType,
    val position: Long
)
