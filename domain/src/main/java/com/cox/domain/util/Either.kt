package com.cox.domain.util

sealed class Either<out L, out R> {
    // Represents the case where there's a value on the left side (error)
    data class Left<out L>(val value: L) : Either<L, Nothing>()

    // Represents the case where there's a value on the right side (success)
    data class Right<out R>(val value: R) : Either<Nothing, R>()

    // Function to handle both cases
    fun <T> fold(fnL: (L) -> T, fnR: (R) -> T): T =
        when (this) {
            is Left -> fnL(value) // fnL = leftOperation
            is Right -> fnR(value) // fnR = rightOperation
        }
}
