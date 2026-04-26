package com.kille.application.port.input.bookmark

import com.kille.application.port.input.UseCase
import com.kille.domain.model.BookmarkId
import com.kille.domain.repository.BookmarkRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteBookmarkUseCase(
    private val bookmarkRepository: BookmarkRepository
) : UseCase<String, Unit> {

    @Transactional
    override fun execute(input: String) {
        bookmarkRepository.deleteById(BookmarkId.fromString(input))
    }
}
