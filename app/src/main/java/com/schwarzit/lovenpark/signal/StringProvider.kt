package com.schwarzit.lovenpark.signal

import android.content.Context
import com.schwarzit.lovenpark.R

class StringProvider(val context: Context) {
    fun getCreatedAt(str: Int, year: String?, month: String?, day: String?, time: String?) =
        context.getString(str, year, month, day, time)

    fun getCategory(str: Int) = context.getString(str)
}