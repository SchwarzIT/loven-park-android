package com.schwarzit.lovenpark

import javax.inject.Inject


class CoreConfig @Inject constructor() {
     val lovenParkBaseUrl: String
        get() = BuildConfig.LovenParkBaseUrl
    val applicationID: String
        get() = BuildConfig.APPLICATION_ID
    val flavour: String
        get() = BuildConfig.FLAVOR
    val versionCode: Int
        get() = BuildConfig.VERSION_CODE
}