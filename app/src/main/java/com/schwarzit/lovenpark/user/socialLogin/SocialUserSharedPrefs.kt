package com.schwarzit.lovenpark.user.socialLogin

import android.content.Context
import com.schwarzit.lovenpark.utils.Util

object SocialUserSharedPrefs {
    fun saveLoggedUser(context: Context, user: String) =
        context
            .getSharedPreferences(SocialLoginSharedPrefKeys.USER_SHARED_PREFS_, Context.MODE_PRIVATE)
            .edit()
            .putString(SocialLoginSharedPrefKeys.USER_LOGGED_KEY, user)
            .commit()

    fun getLoggedUser(context: Context): String? =
        context
            .getSharedPreferences(SocialLoginSharedPrefKeys.USER_SHARED_PREFS_, Context.MODE_PRIVATE)
            .getString(SocialLoginSharedPrefKeys.USER_LOGGED_KEY, Util.EMPTY_STRING)

    fun removeUser(context: Context) =
        context
            .getSharedPreferences(SocialLoginSharedPrefKeys.USER_SHARED_PREFS_, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()

}