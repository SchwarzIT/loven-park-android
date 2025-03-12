package com.schwarzit.lovenpark.keyStore

import android.content.Context
import com.schwarzit.lovenpark.utils.Util

object KeyStoreSharedPrefManager {

    fun saveTokenPairString(
        context: Context,
        pairString: String,
        tokenTypeFirstPairKey: String
    ) =
        context
            .getSharedPreferences(KeyStoreSharedPrefsKeys.TOKENS_SHARED_PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(tokenTypeFirstPairKey, pairString)
            .apply()

    fun getTokenPairString(context: Context, tokenTypePairKey: String) =
        context
            .getSharedPreferences(KeyStoreSharedPrefsKeys.TOKENS_SHARED_PREFS, Context.MODE_PRIVATE)
            .getString(tokenTypePairKey, Util.EMPTY_STRING)

}