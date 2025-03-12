package com.schwarzit.lovenpark.mappin.data.local

import com.schwarzit.lovenpark.data.local.RealmConfigurationProvider
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject

class MapPinsLocalDataSource  @Inject constructor(){

    /**
     * Returns the last saved pins from the local database
     */
    fun getLastPins(): List<MapPinRealm> {
        val realm = Realm.open(RealmConfigurationProvider.getRealmConfig())
        return realm.query<MapPinRealm>().find().toList()
    }

    /**
     * Returns the last favourite saved pins from the local database
     */
    fun getLastFavouritePins(): List<FavouriteMapPinRealm> {
        val realm = Realm.open(RealmConfigurationProvider.getRealmFavouriteConfig())
        return  realm.query<FavouriteMapPinRealm>().find().toList()
    }
}