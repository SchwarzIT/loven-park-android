package com.schwarzit.lovenpark.registration.model

import com.schwarzit.lovenpark.utils.Util.Companion.EMAIL_PATTERN
import java.util.regex.Pattern

class Email(
    val email: String
) : InputData {

    override fun isDataValid(): Boolean =
        Pattern
            .compile(EMAIL_PATTERN)
            .matcher(email)
            .matches()

    override fun isDataEmpty(): Boolean = email.isEmpty()

}
