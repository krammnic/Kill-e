package com.kille.presentation.dto.request

import com.kille.domain.model.BookmarkType
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateBookmarkRequest(
    @field:NotBlank(message = "User ID is required")
    val userId: String,

    @field:NotBlank(message = "Chapter ID is required")
    val chapterId: String,

    @field:NotNull(message = "Bookmark type is required")
    val type: BookmarkType,

    @field:Min(value = 0, message = "Position must be greater than or equal to 0")
    val position: Long
)
