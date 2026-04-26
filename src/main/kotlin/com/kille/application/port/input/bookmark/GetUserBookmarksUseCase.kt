package com.kille.application.port.input.bookmark

import com.kille.application.port.input.UseCase
import com.kille.domain.exception.ValidationException
import com.kille.domain.repository.BookmarkRepository
import com.kille.presentation.dto.response.BookmarkResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetUserBookmarksUseCase(
    private val bookmarkRepository: BookmarkRepository
) : UseCase<String, List<BookmarkResponse>> {

    @Transactional(readOnly = true)
    override fun execute(input: String): List<BookmarkResponse> {
        val userId = try {
            UUID.fromString(input)
        } catch (e: IllegalArgumentException) {
            throw ValidationException("Invalid userId format: $input")
        }

        return bookmarkRepository.findAllByUserId(userId)
            .map { BookmarkResponse.fromDomain(it) }
    }
}
