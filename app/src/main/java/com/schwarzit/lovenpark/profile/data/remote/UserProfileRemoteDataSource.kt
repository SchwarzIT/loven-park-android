package com.schwarzit.lovenpark.profile.data.remote

import com.schwarzit.lovenpark.network.LovenParkService
import retrofit2.Response
import javax.inject.Inject

class UserProfileRemoteDataSource @Inject constructor(val lovenParkService: LovenParkService) {

    suspend fun getUser(): UserRemoteModel? {
        return lovenParkService.getUser().body()
    }

    suspend fun updateUser(
        user: UpdateUserInfo
    ): UpdateUserInfo? {
        return lovenParkService.updateUser(user).body()
    }

    suspend fun deleteProfile(): Response<Unit> {
        return lovenParkService.deleteUser()
    }

}