package com.schwarzit.lovenpark.data.utils

import android.content.Context
import com.schwarzit.lovenpark.data.utils.SharedPrefsKeys.Companion.LAST_APP_START_KEY
import com.schwarzit.lovenpark.utils.Util

object MapPinsSharedPrefHelper {
    fun saveLastData(context: Context, localDateTime: String) =
        context
            .getSharedPreferences(LAST_APP_START_KEY, Context.MODE_PRIVATE)
            .edit()
            .putString(LAST_APP_START_KEY, localDateTime)
            .commit()

    fun getLastSavedDate(context: Context) =
        context
            .getSharedPreferences(LAST_APP_START_KEY, Context.MODE_PRIVATE)
            .getString(LAST_APP_START_KEY, Util.EMPTY_STRING)

    fun removeLastSavedData(context: Context) =
        context
            .getSharedPreferences(LAST_APP_START_KEY, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
}