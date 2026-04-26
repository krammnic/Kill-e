ALTER TABLE chapters
    ADD CONSTRAINT uk_chapters_id_book_id UNIQUE (id, book_id);

CREATE TABLE bookmarks (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    book_id UUID NOT NULL,
    chapter_id UUID NOT NULL,
    bookmark_type VARCHAR(20) NOT NULL,
    position BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_bookmarks_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    CONSTRAINT fk_bookmarks_chapter FOREIGN KEY (chapter_id) REFERENCES chapters(id) ON DELETE CASCADE,
    CONSTRAINT fk_bookmarks_chapter_book FOREIGN KEY (chapter_id, book_id) REFERENCES chapters(id, book_id) ON DELETE CASCADE,
    CONSTRAINT ck_bookmarks_type CHECK (bookmark_type IN ('READING', 'LISTENING')),
    CONSTRAINT ck_bookmarks_position CHECK (position >= 0),
    CONSTRAINT uk_bookmarks_user_chapter_type_position UNIQUE (user_id, chapter_id, bookmark_type, position)
);

CREATE INDEX idx_bookmarks_user_id ON bookmarks(user_id);
CREATE INDEX idx_bookmarks_book_id ON bookmarks(book_id);
CREATE INDEX idx_bookmarks_chapter_id ON bookmarks(chapter_id);
