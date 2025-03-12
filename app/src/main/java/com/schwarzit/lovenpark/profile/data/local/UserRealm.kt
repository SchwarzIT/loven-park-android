package com.schwarzit.lovenpark.profile.data.local

import com.schwarzit.lovenpark.profile.data.domainmodel.UserModel
import com.schwarzit.lovenpark.registration.model.*
import com.schwarzit.lovenpark.utils.Util.Companion.USER_FIRST_NAME_DELIMITER
import io.realm.kotlin.types.RealmObject

open class UserRealm : RealmObject {
    var name: String? = null
    var email: String? = null
    var phoneNumber: String? = null
    var password: String? = null
}

fun UserRealm.toUser() = User(
    Name(this.name.toString()),
    Email(this.email.toString()),
    PhoneNumber(this.phoneNumber.toString()),
    Password(this.password.toString())
)

fun UserRealm.toUserModel() = UserModel(
    name?.split(USER_FIRST_NAME_DELIMITER)?.first().toString(),
    name?.split(USER_FIRST_NAME_DELIMITER)?.last().toString(),
    email.toString(),
    phoneNumber.toString()
)
