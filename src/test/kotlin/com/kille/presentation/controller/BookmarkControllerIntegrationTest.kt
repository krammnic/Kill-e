package com.kille.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.kille.domain.model.Book
import com.kille.domain.model.BookId
import com.kille.domain.model.BookmarkType
import com.kille.domain.model.Chapter
import com.kille.domain.model.ChapterIndex
import com.kille.domain.repository.BookRepository
import com.kille.infrastructure.persistence.repository.BookJpaRepository
import com.kille.infrastructure.persistence.repository.BookmarkJpaRepository
import com.kille.infrastructure.persistence.repository.ChapterJpaRepository
import com.kille.presentation.dto.request.CreateBookmarkRequest
import org.hamcrest.Matchers.everyItem
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItems
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
class BookmarkControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var bookmarkJpaRepository: BookmarkJpaRepository

    @Autowired
    private lateinit var chapterJpaRepository: ChapterJpaRepository

    @Autowired
    private lateinit var bookJpaRepository: BookJpaRepository

    @BeforeEach
    fun setUp() {
        bookmarkJpaRepository.deleteAll()
        chapterJpaRepository.deleteAll()
        bookJpaRepository.deleteAll()
    }

    @Test
    fun `POST - creates bookmark`() {
        val chapter = persistChapter()
        val userId = UUID.randomUUID()
        val request = CreateBookmarkRequest(
            userId = userId.toString(),
            chapterId = chapter.id.value.toString(),
            type = BookmarkType.READING,
            position = 7
        )

        mockMvc.perform(
            post("/api/v1/bookmarks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.userId").value(userId.toString()))
            .andExpect(jsonPath("$.data.bookId").value(chapter.bookId.value.toString()))
            .andExpect(jsonPath("$.data.chapterId").value(chapter.id.value.toString()))
            .andExpect(jsonPath("$.data.type").value("READING"))
            .andExpect(jsonPath("$.data.position").value(7))

        assertEquals(1L, bookmarkJpaRepository.count())
    }

    @Test
    fun `GET - returns bookmarks only for requested user`() {
        val chapter = persistChapter()
        val requestedUserId = UUID.randomUUID()
        val anotherUserId = UUID.randomUUID()

        createBookmark(requestedUserId, chapter.id.value, BookmarkType.READING, 4)
        createBookmark(requestedUserId, chapter.id.value, BookmarkType.LISTENING, 10)
        createBookmark(anotherUserId, chapter.id.value, BookmarkType.READING, 2)

        mockMvc.perform(
            get("/api/v1/bookmarks")
                .param("userId", requestedUserId.toString())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$..userId", everyItem(equalTo(requestedUserId.toString()))))
            .andExpect(jsonPath("$..position", hasItems(4, 10)))
    }

    @Test
    fun `DELETE - removes bookmark`() {
        val chapter = persistChapter()
        val bookmarkId = createBookmark(
            userId = UUID.randomUUID(),
            chapterId = chapter.id.value,
            type = BookmarkType.READING,
            position = 9
        )

        mockMvc.perform(delete("/api/v1/bookmarks/{id}", bookmarkId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))

        assertEquals(0L, bookmarkJpaRepository.count())
    }

    private fun persistChapter(): Chapter {
        val bookId = BookId.generate()
        val chapter = Chapter.createWithText(
            bookId = bookId,
            index = ChapterIndex(0),
            title = "Chapter 1",
            text = "Chapter text for bookmarks"
        )
            .addAudio("https://example.com/audio.mp3")
            .updateDuration(120_000)

        val book = Book.createWithId(
            id = bookId,
            title = "Bookmark Book",
            author = "Author",
            language = "ru",
            chapters = listOf(chapter),
            audio = true,
            text = true
        )

        bookRepository.save(book)
        return chapter
    }

    private fun createBookmark(
        userId: UUID,
        chapterId: UUID,
        type: BookmarkType,
        position: Long
    ): String {
        val response = mockMvc.perform(
            post("/api/v1/bookmarks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsBytes(
                        CreateBookmarkRequest(
                            userId = userId.toString(),
                            chapterId = chapterId.toString(),
                            type = type,
                            position = position
                        )
                    )
                )
        )
            .andExpect(status().isCreated)
            .andReturn()
            .response
            .contentAsString

        return objectMapper.readTree(response)
            .path("data")
            .path("id")
            .asText()
    }
}
