package com.kille.domain.model

import com.kille.domain.exception.DomainException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class BookmarkTest {

    @Test
    fun `CREATE - creates reading bookmark for chapter with text`() {
        val chapter = Chapter.createWithText(
            bookId = BookId.generate(),
            index = ChapterIndex(0),
            title = "Chapter 1",
            text = "Bookmark text"
        )
        val userId = UUID.randomUUID()

        val bookmark = Bookmark.create(
            userId = userId,
            chapter = chapter,
            type = BookmarkType.READING,
            position = 5
        )

        assertNotNull(bookmark.id)
        assertEquals(userId, bookmark.userId)
        assertEquals(chapter.bookId, bookmark.bookId)
        assertEquals(chapter.id, bookmark.chapterId)
        assertEquals(BookmarkType.READING, bookmark.type)
        assertEquals(5, bookmark.position)
    }

    @Test
    fun `CREATE - fails when reading position exceeds chapter text length`() {
        val chapter = Chapter.createWithText(
            bookId = BookId.generate(),
            index = ChapterIndex(0),
            title = "Chapter 1",
            text = "Text"
        )

        assertThrows<DomainException> {
            Bookmark.create(
                userId = UUID.randomUUID(),
                chapter = chapter,
                type = BookmarkType.READING,
                position = 5
            )
        }
    }

    @Test
    fun `CREATE - fails when listening bookmark is created for chapter without audio`() {
        val chapter = Chapter.createWithText(
            bookId = BookId.generate(),
            index = ChapterIndex(0),
            title = "Chapter 1",
            text = "Text"
        )

        assertThrows<DomainException> {
            Bookmark.create(
                userId = UUID.randomUUID(),
                chapter = chapter,
                type = BookmarkType.LISTENING,
                position = 0
            )
        }
    }
}
