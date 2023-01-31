package com.group.libraryapp.service.book

import com.group.libraryapp.domain.DomainCreator
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    @BeforeEach
    fun clean() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun saveBook_success() {
        // given
        val request = BookRequest("화산귀환", BookType.COMPUTER)

        // when
        bookService.saveBook(request)

        // then
        val result = bookRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("화산귀환")
        assertThat(result[0].type).isEqualTo(BookType.COMPUTER)
    }

    @Test
    fun loadBook_success() {
        // given
        val savedBook = bookRepository.save(DomainCreator.createBook("화산귀환", BookType.COMPUTER))
        val savedUser = userRepository.save(User("홍영준", 29))
        val request = BookLoanRequest("홍영준", "화산귀환")

        // when
        bookService.loanBook(request)

        // then
        val result = userLoanHistoryRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].bookName).isEqualTo(savedBook.name)
        assertThat(result[0].user.id).isEqualTo(savedUser.id)
        assertThat(result[0].status).isEqualTo(UserLoanStatus.LOANED)
    }

    @Test
    fun loadBook_throw_IllegalArgumentException_when_loanHistory_is_exist() {
        // given
        bookRepository.save(DomainCreator.createBook("화산귀환", BookType.COMPUTER))
        val savedUser = userRepository.save(User("홍영준", 29))
        userLoanHistoryRepository.save(DomainCreator.createUserLoanHistory(savedUser, "화산귀환"))
        val request = BookLoanRequest("홍영준", "화산귀환")

        // when & then
        assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.also { exception ->
            assertThat(exception.message).isEqualTo("진작 대출되어 있는 책입니다")
        }
    }

    @Test
    fun returnBook_success() {
        // given
        bookRepository.save(DomainCreator.createBook("화산귀환", BookType.COMPUTER))
        val savedUser = userRepository.save(User("홍영준", 29))
        userLoanHistoryRepository.save(DomainCreator.createUserLoanHistory(savedUser, "화산귀환"))
        val request = BookReturnRequest("홍영준", "화산귀환")

        // when
        bookService.returnBook(request)

        // then
        val result = userLoanHistoryRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }

    @Test
    fun countLoanedBook_success() {
        // given
        val savedUser = userRepository.save(User("홍영준", 29))
        userLoanHistoryRepository.saveAll(listOf(
            DomainCreator.createUserLoanHistory(savedUser, "A"),
            DomainCreator.createUserLoanHistory(savedUser, "B", UserLoanStatus.RETURNED),
            DomainCreator.createUserLoanHistory(savedUser, "C", UserLoanStatus.RETURNED),
        ))

        // when
        val result = bookService.countLoanedBook()

        // then
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun getBookStatistics_success() {
        // given
        bookRepository.saveAll(listOf(
            DomainCreator.createBook("A", BookType.COMPUTER),
            DomainCreator.createBook("B", BookType.COMPUTER),
            DomainCreator.createBook("C", BookType.LANGUAGE),
        ))

        // when
        val result = bookService.getBookStatistics()

        // then
        assertThat(result).hasSize(2)
        assertThat(result.first { r -> r.type == BookType.COMPUTER }.count).isEqualTo(2)
        assertThat(result.first { r -> r.type == BookType.LANGUAGE }.count).isEqualTo(1)
    }
}