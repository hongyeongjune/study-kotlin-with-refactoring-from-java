package com.group.libraryapp.domain

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus

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

        fun createUserLoanHistory(
            user: User,
            bookName: String,
            status: UserLoanStatus = UserLoanStatus.LOANED,
            id: Long? = null
        ): UserLoanHistory {
            return UserLoanHistory(
                user = user,
                bookName = bookName,
                status = status,
                id = id,
            )
        }
    }
}