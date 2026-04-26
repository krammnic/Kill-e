package com.kille.domain.model

import com.kille.domain.exception.DomainException
import java.util.UUID

@JvmInline
value class BookmarkId(val value: UUID) {
    init {
        require(value.toString().isNotBlank()) { "BookmarkId cannot be empty" }
    }

    companion object {
        fun generate(): BookmarkId = BookmarkId(UUID.randomUUID())

        fun fromString(value: String): BookmarkId {
            return try {
                BookmarkId(UUID.fromString(value))
            } catch (e: IllegalArgumentException) {
                throw DomainException("Invalid BookmarkId format: $value", e)
            }
        }
    }

    override fun toString(): String = value.toString()
}
