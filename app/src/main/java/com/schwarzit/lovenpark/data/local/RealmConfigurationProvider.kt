package com.schwarzit.lovenpark.data.local

import com.schwarzit.lovenpark.mappin.data.local.FavouriteMapPinRealm
import com.schwarzit.lovenpark.mappin.data.local.MapPinRealm
import com.schwarzit.lovenpark.profile.data.local.UserRealm
import com.schwarzit.lovenpark.profile.signals.data.local.SignalRealm
import com.schwarzit.lovenpark.utils.Util.Companion.SCHEMA_FAVOURITE_NAME
import com.schwarzit.lovenpark.utils.Util.Companion.SCHEMA_FAVOURITE_VERSION
import com.schwarzit.lovenpark.utils.Util.Companion.SCHEMA_MAIN_NAME
import com.schwarzit.lovenpark.utils.Util.Companion.SCHEMA_VERSION
import io.realm.kotlin.RealmConfiguration

object RealmConfigurationProvider {

    private val realmConfig =
        RealmConfiguration.Builder(
            setOf(
                MapPinRealm::class,
                UserRealm::class,
                SignalRealm::class,
            )
        )
            .name(SCHEMA_MAIN_NAME)
            .schemaVersion(SCHEMA_VERSION)
            .deleteRealmIfMigrationNeeded()
            .build()

    private val realmFavouriteConfig =
        RealmConfiguration.Builder(setOf(FavouriteMapPinRealm::class))
            .name(SCHEMA_FAVOURITE_NAME)
            .schemaVersion(SCHEMA_FAVOURITE_VERSION)
            .deleteRealmIfMigrationNeeded()
            .build()

    fun getRealmConfig() = realmConfig

    fun getRealmFavouriteConfig() = realmFavouriteConfig
}