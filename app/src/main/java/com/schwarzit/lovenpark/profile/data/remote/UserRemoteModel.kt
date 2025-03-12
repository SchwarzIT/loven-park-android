package com.schwarzit.lovenpark.profile.data.remote

import com.google.gson.annotations.SerializedName
import com.schwarzit.lovenpark.profile.data.domainmodel.UserModel
import com.schwarzit.lovenpark.profile.data.local.UserRealm
import com.schwarzit.lovenpark.utils.Util.Companion.EMPTY_STRING
import com.schwarzit.lovenpark.utils.Util.Companion.USER_FIRST_NAME_DELIMITER

data class UserRemoteModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("profilePicture")
    val profilePicture: String?,
    @SerializedName("badge")
    val badge: String
)

fun UserRemoteModel.toUserModel() = UserModel(
    fullName.split(USER_FIRST_NAME_DELIMITER).first(),
    fullName.split(USER_FIRST_NAME_DELIMITER).last(),
    email,
    phoneNumber ?: EMPTY_STRING
)

fun UserRemoteModel.toUserRealm(): UserRealm {
    val userRealm = UserRealm()
    this.apply {
        userRealm.name = fullName
        userRealm.email = email
        userRealm.phoneNumber = phoneNumber
        userRealm.password = EMPTY_STRING
    }
    return userRealm
}