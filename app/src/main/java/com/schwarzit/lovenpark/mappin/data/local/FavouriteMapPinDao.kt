package com.schwarzit.lovenpark.mappin.data.local

import com.schwarzit.lovenpark.data.local.RealmConfigurationProvider
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavouriteMapPinDao {

    suspend fun getFavouriteMapPinById(id: String): String? {
        return withContext(Dispatchers.IO) {

            val realm = Realm.open(RealmConfigurationProvider.getRealmFavouriteConfig())

            val dbMapPin = realm.query<FavouriteMapPinRealm>("id == '$id'")
                .find().firstOrNull() ?: return@withContext null

            val mapPin = dbMapPin.id
            realm.close()
            mapPin
        }
    }

    suspend fun saveFavouriteMapPinId(mapPinIdRealm: FavouriteMapPinRealm) {
        withContext(Dispatchers.IO) {

            val realm = Realm.open(RealmConfigurationProvider.getRealmFavouriteConfig())

            realm.write {
                copyToRealm(mapPinIdRealm, UpdatePolicy.ALL)
            }
            realm.close()
        }
    }

    suspend fun deleteFavouriteMapPinId(mapPinIdRealm: FavouriteMapPinRealm) {
        withContext(Dispatchers.IO) {

            val realm = Realm.open(RealmConfigurationProvider.getRealmFavouriteConfig())

            val dbMapPin = realm.query<FavouriteMapPinRealm>("id == '${mapPinIdRealm.id}'")
                .find().firstOrNull()

            realm.writeBlocking {
                if (dbMapPin != null) {
                    findLatest(dbMapPin)?.also {
                        delete(it)
                    }
                }
            }
            realm.close()
        }
    }
}