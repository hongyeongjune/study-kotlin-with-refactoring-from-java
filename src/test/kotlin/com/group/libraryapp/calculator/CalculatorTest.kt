package com.group.libraryapp.calculator

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class CalculatorTest {

    @Test
    fun add_success() {
        // given
        val calculator = Calculator(5)
        val expected = Calculator(8)

        // when
        calculator.add(3)

        // then
        assertThat(calculator).isEqualTo(expected)
    }

    @Test
    fun minus_success() {
        // given
        val calculator = Calculator(5)
        val expected = Calculator(2)

        // when
        calculator.minus(3)

        // then
        assertThat(calculator).isEqualTo(expected)
    }

    @Test
    fun multiply_success() {
        // given
        val calculator = Calculator(5)
        val expected = Calculator(15)

        // when
        calculator.multiply(3)

        // then
        assertThat(calculator).isEqualTo(expected)
    }

    @Test
    fun divide_success() {
        // given
        val calculator = Calculator(5)
        val expected = Calculator(1)

        // when
        calculator.divide(3)

        // then
        assertThat(calculator).isEqualTo(expected)
    }

    @Test
    fun divide_throw_illegalArgumentException_when_operand_is_zero() {
        // given
        val calculator = Calculator(5)

        // when & then
        assertThrows<IllegalArgumentException> {
            calculator.divide(0)
        }.also { exception ->
            assertThat(exception.message).isEqualTo("0 으로 나눌 수 없습니다.")
        }
    }

}