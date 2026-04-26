package com.kille.infrastructure.persistence.entity

import com.kille.domain.model.BookmarkType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(
    name = "bookmarks",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_bookmarks_user_chapter_type_position",
            columnNames = ["user_id", "chapter_id", "bookmark_type", "position"]
        )
    ],
    indexes = [
        Index(name = "idx_bookmarks_user_id", columnList = "user_id"),
        Index(name = "idx_bookmarks_book_id", columnList = "book_id"),
        Index(name = "idx_bookmarks_chapter_id", columnList = "chapter_id")
    ]
)
class BookmarkJpaEntity(
    @Id
    @Column(columnDefinition = "UUID")
    val id: UUID,

    @Column(name = "user_id", nullable = false, columnDefinition = "UUID")
    val userId: UUID,

    @Column(name = "book_id", nullable = false, columnDefinition = "UUID")
    val bookId: UUID,

    @Column(name = "chapter_id", nullable = false, columnDefinition = "UUID")
    val chapterId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(name = "bookmark_type", nullable = false, length = 20)
    val type: BookmarkType,

    @Column(name = "position", nullable = false)
    val position: Long,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id", insertable = false, updatable = false)
    var book: BookEntityJpa? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id", referencedColumnName = "id", insertable = false, updatable = false)
    var chapter: ChapterJpaEntity? = null
}
