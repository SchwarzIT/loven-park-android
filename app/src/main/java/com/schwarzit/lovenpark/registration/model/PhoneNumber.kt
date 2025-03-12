package com.schwarzit.lovenpark.registration.model

import java.util.regex.Pattern

class PhoneNumber(
    var phoneNumber: String
) : InputData {

    override fun isDataValid(): Boolean = Pattern
        .compile("^([+ ]?\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- ]?\\d{3}[- ]?\\d{1,6}$")
        .matcher(phoneNumber)
        .matches()

    override fun isDataEmpty(): Boolean = phoneNumber.isEmpty()

}
