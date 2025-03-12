package com.schwarzit.lovenpark.data.utils

import android.content.Context
import com.schwarzit.lovenpark.data.utils.SharedPrefsKeys.Companion.USER_SHARED_PREFS
import com.schwarzit.lovenpark.data.utils.SharedPrefsKeys.Companion.USER_SHARED_PREFS_EMAIL_KEY
import com.schwarzit.lovenpark.data.utils.SharedPrefsKeys.Companion.YEAR
import com.schwarzit.lovenpark.utils.Util.Companion.EMPTY_STRING

object UserSharedPrefHelper {

    fun saveLoggedUserEmail(context: Context, userEmail: String) =
        context
            .getSharedPreferences(USER_SHARED_PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(USER_SHARED_PREFS_EMAIL_KEY, userEmail)
            .apply()

    fun getLoggedUserEmail(context: Context): String? =
        context
            .getSharedPreferences(USER_SHARED_PREFS, Context.MODE_PRIVATE)
            .getString(USER_SHARED_PREFS_EMAIL_KEY, EMPTY_STRING)

    fun removeUserData(context: Context) =
        context
            .getSharedPreferences(USER_SHARED_PREFS, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()

    fun saveSignalYear(context: Context, year: String) =
        context
            .getSharedPreferences(USER_SHARED_PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(YEAR, year)
            .apply()

    fun getSignalYear(context: Context): String? =
        context
            .getSharedPreferences(USER_SHARED_PREFS, Context.MODE_PRIVATE)
            .getString(YEAR, EMPTY_STRING)

    fun removeSignalYear(context: Context) =
        context
            .getSharedPreferences(USER_SHARED_PREFS, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
}