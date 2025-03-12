package com.schwarzit.lovenpark.profile.signals.data.local

import com.schwarzit.lovenpark.data.local.RealmConfigurationProvider
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalModel
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignalDAO {

    suspend fun saveSignal(signalRealm: SignalRealm) {
        withContext(Dispatchers.IO) {

            val realm = Realm.open(RealmConfigurationProvider.getRealmConfig())

            realm.write {
                copyToRealm(signalRealm, UpdatePolicy.ALL)
            }
            realm.close()
        }
    }

    suspend fun getSignalById(id: String): SignalModel? {
        return withContext(Dispatchers.IO) {

            val realm = Realm.open(RealmConfigurationProvider.getRealmConfig())

            val dbSignal = realm.query<SignalRealm>("id == '$id'")
                .find().firstOrNull() ?: return@withContext null

            val signal = dbSignal.toSignalModel()
            realm.close()
            signal
        }
    }

}