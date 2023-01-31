package com.group.libraryapp.dto.book.response

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookType

data class BookStatResponse(
    val type: BookType,
    var count: Int,
) {
    companion object {
        fun of(type: BookType, books: List<Book>): BookStatResponse {
            return BookStatResponse(
                type = type,
                count = books.size,
            )
        }
    }

    fun plusOne() {
        this.count++
    }
}
