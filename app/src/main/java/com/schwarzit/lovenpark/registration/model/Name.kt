package com.schwarzit.lovenpark.registration.model

import com.schwarzit.lovenpark.utils.Util.Companion.NAME_PATTERN
import java.util.regex.Pattern

class Name(
    var name: String
) : InputData {
    override fun isDataValid(): Boolean =
        Pattern
            .compile(NAME_PATTERN)
            .matcher(name)
            .matches()

    override fun isDataEmpty(): Boolean = name.isEmpty()

}