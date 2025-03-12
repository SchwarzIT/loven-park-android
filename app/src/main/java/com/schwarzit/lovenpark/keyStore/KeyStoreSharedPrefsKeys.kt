package com.schwarzit.lovenpark.keyStore

class KeyStoreSharedPrefsKeys {
    companion object {
        const val TOKENS_SHARED_PREFS = "tokensSharedPrefs"

        const val ACCESS_TOKEN_PAIR_FIRST_KEY = "ACCESS_TOKEN_PAIR_FIRST"
        const val ACCESS_TOKEN_PAIR_SECOND_KEY = "ACCESS_TOKEN_PAIR_SECOND"

        const val REFRESH_TOKEN_PAIR_FIRST_KEY = "REFRESH_TOKEN_PAIR_FIRST"
        const val REFRESH_TOKEN_PAIR_SECOND_KEY = "REFRESH_TOKEN_PAIR_SECOND"

        const val ID_TOKEN_TOKEN_PAIR_FIRST_KEY = "ID_TOKEN_PAIR_FIRST"
        const val ID_TOKEN_TOKEN_PAIR_SECOND_KEY = "ID_TOKEN_PAIR_SECOND"

        const val CIPHER_TRANSFORMATION = "AES/CBC/NoPadding"
        const val KEY_ALIAS = "MyKeyAlias"
        const val ANDROID_KEY_STORE = "AndroidKeyStore"
    }
}