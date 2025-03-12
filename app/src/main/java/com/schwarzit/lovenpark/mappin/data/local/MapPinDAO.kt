package com.schwarzit.lovenpark.mappin.data.local

import com.schwarzit.lovenpark.data.local.RealmConfigurationProvider
import com.schwarzit.lovenpark.mappin.domain.MapPinUIModel
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MapPinDAO {

    suspend fun saveMapPin(pinRealm: MapPinRealm) {
        withContext(Dispatchers.IO) {

            val realm = Realm.open(RealmConfigurationProvider.getRealmConfig())

            realm.write {
                copyToRealm(pinRealm, UpdatePolicy.ALL)
            }
            realm.close()
        }
    }

    suspend fun getMapPinById(id: String): MapPinUIModel? {
        return withContext(Dispatchers.IO) {

            val realm = Realm.open(RealmConfigurationProvider.getRealmConfig())

            val dbMapPin = realm.query<MapPinRealm>("id == '$id'")
                .find().firstOrNull() ?: return@withContext null

            val mapPin = dbMapPin.toMapPinUIModel()
            realm.close()
            mapPin
        }
    }
}