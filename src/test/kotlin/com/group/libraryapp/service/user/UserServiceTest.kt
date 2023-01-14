package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
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
        val result = userService.users

        // then
        assertThat(result).hasSize(3)
        assertThat(result).extracting("name").containsExactlyInAnyOrder("홍영준", "네이버웹툰", "네이버시리즈")
        assertThat(result).extracting("age").containsExactlyInAnyOrder(29, 7, null)
    }

    @Test
    fun updateUserName_success() {
        // given
        val saved = userRepository.save(User("홍영준", 29))
        val request = UserUpdateRequest(saved.id, "네이버웹툰")

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
}