CREATE TABLE books (
    id UUID PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    language VARCHAR(50) NOT NULL,
    cover_url VARCHAR(500),
    has_audio BOOLEAN NOT NULL,
    has_text BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE chapters (
    id UUID PRIMARY KEY,
    book_id UUID NOT NULL,
    chapter_index INTEGER NOT NULL,
    title VARCHAR(500),
    text TEXT,
    audio_url VARCHAR(2000),
    timing_url VARCHAR(2000),
    duration_ms BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_chapters_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

CREATE INDEX idx_chapters_book_id ON chapters(book_id);
CREATE INDEX idx_chapters_book_id_chapter_index ON chapters(book_id, chapter_index);
