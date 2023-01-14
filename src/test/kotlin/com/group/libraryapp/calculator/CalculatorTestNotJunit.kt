package com.group.libraryapp.calculator

fun main() {
    val test = CalculatorTestNotJunit()
    test.add_success()
    test.minus_success()
    test.multiply_success()
    test.divide_success()
    test.divide_throw_illegalArgumentException_when_operand_is_zero()
}

class CalculatorTestNotJunit {

    fun add_success() {
        // given
        val calculator = Calculator(5)
        val expected = Calculator(8)

        // when
        calculator.add(3)

        // then
        if (calculator != expected) {
            throw IllegalStateException()
        }
    }

    fun minus_success() {
        // given
        val calculator = Calculator(5)
        val expected = Calculator(2)

        // when
        calculator.minus(3)

        // then
        if (calculator != expected) {
            throw IllegalStateException()
        }
    }

    fun multiply_success() {
        // given
        val calculator = Calculator(5)
        val expected = Calculator(15)

        // when
        calculator.multiply(3)

        // then
        if (calculator != expected) {
            throw IllegalStateException()
        }
    }

    fun divide_success() {
        // given
        val calculator = Calculator(5)
        val expected = Calculator(1)

        // when
        calculator.divide(3)

        // then
        if (calculator != expected) {
            throw IllegalStateException()
        }
    }

    fun divide_throw_illegalArgumentException_when_operand_is_zero() {
        // given
        val calculator = Calculator(5)

        // when & then
        try {
            calculator.divide(0)
        } catch (e: IllegalArgumentException) {
            return
        } catch (e: Exception) {
            throw IllegalStateException("예상하지 못한 예외가 발생하였습니다.")
        }
        throw IllegalStateException("기대하는 예외가 발생하지 않았습니다.")
    }
}