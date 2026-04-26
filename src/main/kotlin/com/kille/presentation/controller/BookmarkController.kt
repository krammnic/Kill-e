package com.kille.presentation.controller

import com.kille.application.port.input.bookmark.CreateBookmarkCommand
import com.kille.application.port.input.bookmark.CreateBookmarkUseCase
import com.kille.application.port.input.bookmark.DeleteBookmarkUseCase
import com.kille.application.port.input.bookmark.GetUserBookmarksUseCase
import com.kille.presentation.dto.request.CreateBookmarkRequest
import com.kille.presentation.dto.response.ApiResponse
import com.kille.presentation.dto.response.BookmarkResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/bookmarks")
@Tag(name = "Bookmarks", description = "API для управления закладками пользователя")
class BookmarkController(
    private val createBookmarkUseCase: CreateBookmarkUseCase,
    private val getUserBookmarksUseCase: GetUserBookmarksUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase
) {

    @PostMapping
    @Operation(summary = "Создать закладку", description = "Создаёт новую закладку для чтения или прослушивания главы")
    fun create(
        @Valid @RequestBody request: CreateBookmarkRequest
    ): ResponseEntity<ApiResponse<BookmarkResponse>> {
        val command = CreateBookmarkCommand(
            userId = request.userId,
            chapterId = request.chapterId,
            type = request.type,
            position = request.position
        )
        val result = createBookmarkUseCase.execute(command)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(result, "Закладка успешно создана"))
    }

    @GetMapping
    @Operation(summary = "Получить закладки пользователя", description = "Возвращает все закладки для указанного пользователя")
    fun getAllByUser(@RequestParam userId: String): ResponseEntity<ApiResponse<List<BookmarkResponse>>> {
        val result = getUserBookmarksUseCase.execute(userId)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить закладку", description = "Удаляет закладку по идентификатору")
    fun delete(@PathVariable id: String): ResponseEntity<ApiResponse<Unit>> {
        deleteBookmarkUseCase.execute(id)
        return ResponseEntity.ok(ApiResponse.success(Unit, "Закладка успешно удалена"))
    }
}
