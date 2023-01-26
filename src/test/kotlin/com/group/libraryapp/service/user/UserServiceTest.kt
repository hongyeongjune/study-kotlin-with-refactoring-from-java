package com.group.libraryapp.service.user

import com.group.libraryapp.domain.DomainCreator
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    @BeforeEach
    fun user_clean() {
        userRepository.deleteAll()
    }

    @Test
    fun saveUser_success() {
        // given
        val request = UserCreateRequest("홍영준", 29)

        // when
        userService.saveUser(request)

        // then
        val result = userRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("홍영준")
        assertThat(result[0].age).isEqualTo(29)
    }

    @Test
    fun saveUser_success_when_age_is_null() {
        // given
        val request = UserCreateRequest("홍영준", null)

        // when
        userService.saveUser(request)

        // then
        val result = userRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("홍영준")
        assertThat(result[0].age).isNull()
    }

    @Test
    fun getUser_success() {
        // given
        userRepository.saveAll(listOf(
            User("홍영준", 29),
            User("네이버웹툰", 7),
            User("네이버시리즈", null)
        ))

        // when
        val result = userService.getUsers()

        // then
        assertThat(result).hasSize(3)
        assertThat(result).extracting("name").containsExactlyInAnyOrder("홍영준", "네이버웹툰", "네이버시리즈")
        assertThat(result).extracting("age").containsExactlyInAnyOrder(29, 7, null)
    }

    @Test
    fun updateUserName_success() {
        // given
        val saved = userRepository.save(User("홍영준", 29))
        val request = UserUpdateRequest(saved.id!!, "네이버웹툰")

        // when
        userService.updateUserName(request)

        // then
        val result = userRepository.findAll()[0]
        assertThat(result.name).isEqualTo("네이버웹툰")
    }

    @Test
    fun deleteUser_success() {
        // given
        userRepository.save(User("홍영준", 29))

        // when
        userService.deleteUser("홍영준")

        // then
        assertThat(userRepository.findAll()).isEmpty()
    }

    @Test
    fun getUserLoanHistories_success_when_loan_is_null() {
        // given
        val expected = userRepository.save(User("홍영준", 29))

        // when
        val result = userService.getUserLoanHistories()

        // then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo(expected.name)
        assertThat(result[0].books).isEmpty()
    }

    @Test
    fun getUserLoanHistories_success_when_user_have_many_loan() {
        // given
        val savedUser = userRepository.save(User("홍영준", 29))
        userLoanHistoryRepository.saveAll(listOf(
            DomainCreator.createUserLoanHistory(savedUser, "화산귀환", UserLoanStatus.LOANED),
            DomainCreator.createUserLoanHistory(savedUser, "광마회귀", UserLoanStatus.LOANED),
            DomainCreator.createUserLoanHistory(savedUser, "나노마신", UserLoanStatus.RETURNED),
        ))

        // when
        val result = userService.getUserLoanHistories()

        // then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo(savedUser.name)
        assertThat(result[0].books).hasSize(3)
        assertThat(result[0].books).extracting("name").containsExactlyInAnyOrder("화산귀환", "광마회귀", "나노마신")
        assertThat(result[0].books).extracting("isReturn").containsExactlyInAnyOrder(false, false, true)
    }
}