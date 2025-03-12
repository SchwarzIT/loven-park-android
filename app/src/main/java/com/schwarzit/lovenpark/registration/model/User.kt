package com.schwarzit.lovenpark.registration.model

import com.schwarzit.lovenpark.profile.data.local.UserRealm

class User(
    var name: Name,
    val email: Email,
    var phoneNumber: PhoneNumber,
    var password: Password
)

fun User.toUserRealm(): UserRealm {
    val userRealm = UserRealm()
    this.apply {
        userRealm.name = name.name
        userRealm.email = email.email
        userRealm.phoneNumber = phoneNumber.phoneNumber
        userRealm.password = password.password
    }
    return userRealm
}
