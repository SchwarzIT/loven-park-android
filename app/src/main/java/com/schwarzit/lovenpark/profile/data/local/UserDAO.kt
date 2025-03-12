package com.schwarzit.lovenpark.profile.data.local

import com.schwarzit.lovenpark.data.local.RealmConfigurationProvider
import com.schwarzit.lovenpark.profile.data.domainmodel.UserModel
import com.schwarzit.lovenpark.registration.model.*
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserDAO {

    suspend fun saveUser(user: UserRealm) {
        withContext(Dispatchers.IO) {
            val realm = Realm.open(RealmConfigurationProvider.getRealmConfig())

            realm.write {
                copyToRealm(user)
            }
            realm.close()
        }
    }

    suspend fun getUser(email: Email): User? {
        return withContext(Dispatchers.IO) {
            val realm = Realm.open(RealmConfigurationProvider.getRealmConfig())

            val dbUser = realm.query<UserRealm>("email == '${email.email}'").find().firstOrNull()
                ?: return@withContext null

            val user = dbUser.toUser()
            realm.close()
            user
        }
    }

    suspend fun getUserModel(email: Email): UserModel? {
        return withContext(Dispatchers.IO) {
            val realm = Realm.open(RealmConfigurationProvider.getRealmConfig())

            val dbUser = realm.query<UserRealm>("email == '${email.email}'").find().firstOrNull()
                ?: return@withContext null

            val user = dbUser.toUserModel()
            realm.close()
            user
        }
    }
}