package com.schwarzit.lovenpark.user.google

data class GoogleAccount(
    val id: String?,
    val email: String?,
    val idToken: String?,
    val givenName: String?,
    val familyName: String?,
    val displayName: String?
)
