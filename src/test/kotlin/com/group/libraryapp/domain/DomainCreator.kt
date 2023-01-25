package com.group.libraryapp.domain

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookType

class DomainCreator {
    companion object {
        fun createBook(
            name: String,
            type: BookType,
            id: Long? = null,
        ): Book {
            return Book(
                id = id,
                name = name,
                type = type,
            )
        }
    }
}