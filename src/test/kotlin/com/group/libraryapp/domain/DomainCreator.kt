package com.group.libraryapp.domain

import com.group.libraryapp.domain.book.Book

class DomainCreator {
    companion object {
        fun createBook(
            name: String,
            type: String,
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