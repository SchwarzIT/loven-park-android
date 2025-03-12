package com.schwarzit.lovenpark.profile.data.local

import android.content.Context
import com.schwarzit.lovenpark.data.local.RealmConfigurationProvider
import com.schwarzit.lovenpark.data.utils.UserSharedPrefHelper
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject

class UserProfileLocalDataSource @Inject constructor() {

    fun getLastUser(context: Context): UserRealm? {
        val realm = Realm.open(RealmConfigurationProvider.getRealmConfig())
        val userEmail = UserSharedPrefHelper.getLoggedUserEmail(context)
        return realm.query<UserRealm>("email == '$userEmail'").find().firstOrNull()
    }
}