package com.schwarzit.lovenpark.profile.signals.data.local

import android.content.Context
import com.schwarzit.lovenpark.data.local.RealmConfigurationProvider
import com.schwarzit.lovenpark.profile.data.UserProfileRepository
import com.schwarzit.lovenpark.utils.Util.Companion.EMPTY_STRING
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject

class SignalsLocalDataSource @Inject constructor(
    private val userRepository: UserProfileRepository
) {

    /**
     * Returns the last saved active signals from the local database
     */
    fun getLastActiveSignals(): List<SignalRealm> {
        val realm = Realm.open(RealmConfigurationProvider.getRealmConfig())

        return realm.query<SignalRealm>().find().toList()
    }

    /**
     * Returns the last saved user signals from the local database
     */
    fun getLastUserSignals(context: Context): List<SignalRealm> {
        val realm = Realm.open(RealmConfigurationProvider.getRealmConfig())

        val userEmail = userRepository.getSavedUserFromDatabase(context)?.email ?: EMPTY_STRING
        return realm.query<SignalRealm>("createdByUserEmail == '$userEmail'").find().toList()
    }

    /**
     * Returns the last saved others' signals from the local database
     */
    fun getLastOthersSignals(context: Context): List<SignalRealm> {
        val realm = Realm.open(RealmConfigurationProvider.getRealmConfig())

        val userEmail = userRepository.getSavedUserFromDatabase(context)?.email ?: EMPTY_STRING
        return realm.query<SignalRealm>("createdByUserEmail != '$userEmail'").find().toList()
    }

}