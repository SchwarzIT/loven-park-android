package com.schwarzit.lovenpark.profile.data

import android.content.Context
import com.schwarzit.lovenpark.profile.data.domainmodel.UserModel
import com.schwarzit.lovenpark.profile.data.local.UserDAO
import com.schwarzit.lovenpark.profile.data.local.UserProfileLocalDataSource
import com.schwarzit.lovenpark.profile.data.local.toUserModel
import com.schwarzit.lovenpark.profile.data.remote.*
import com.schwarzit.lovenpark.registration.model.Email
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class UserProfileRepository @Inject constructor(
    private val remoteDataSource: UserProfileRemoteDataSource,
    private val localDataSource: UserProfileLocalDataSource
) {
    private val userDAO: UserDAO = UserDAO()

    suspend fun getUserRemote(): UserRemoteModel =
        remoteDataSource.getUser() ?: throw IllegalStateException()

    suspend fun getUser(): UserModel? = remoteDataSource.getUser()?.toUserModel()

    fun saveUserInDatabase(user: UserRemoteModel) {
        CoroutineScope(Dispatchers.IO).launch {
            if (userDAO.getUserModel(Email(user.email)) == null) {
                userDAO.saveUser(user.toUserRealm())
            }
        }
    }

    fun getSavedUserFromDatabase(context: Context): UserModel? =
        localDataSource.getLastUser(context)?.toUserModel()

    suspend fun updateUser(user: UpdateUserInfo): UpdateUserInfo? {
        return remoteDataSource.updateUser(user)
    }

    suspend fun deleteProfile(): Response<Unit> {
        return remoteDataSource.deleteProfile()
    }
}