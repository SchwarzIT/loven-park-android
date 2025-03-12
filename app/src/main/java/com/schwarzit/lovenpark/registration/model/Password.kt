package com.schwarzit.lovenpark.registration.model

import com.schwarzit.lovenpark.utils.Util.Companion.PASSWORD_MINIMUM_LENGTH

class Password(
    var password: String
) : InputData {
    // the password is valid when:
    // 8 symbols min length = 1 upper case + 1 digit + 1 special symbol + 4 other
    override fun isDataValid(): Boolean =
        upperCaseMinimumChecker()
                && digitMinimumChecker()
                && specialSymbolMinimumChecker()
                && minimumLengthChecker()

    override fun isDataEmpty(): Boolean = password.isEmpty()

    private fun upperCaseMinimumChecker(): Boolean = password.any { it.isUpperCase() }

    private fun digitMinimumChecker(): Boolean = password.any { it.isDigit() }

    private fun specialSymbolMinimumChecker(): Boolean =
        password.any { !it.isDigit() && !it.isLetter() }

    private fun minimumLengthChecker(): Boolean = password.length >= PASSWORD_MINIMUM_LENGTH
}
