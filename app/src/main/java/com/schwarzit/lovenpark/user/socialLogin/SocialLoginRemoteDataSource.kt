package com.schwarzit.lovenpark.user.socialLogin

import com.schwarzit.lovenpark.network.LovenParkService
import com.schwarzit.lovenpark.utils.Util.Companion.BEARER_TOKEN
import com.schwarzit.lovenpark.user.TokenInfo
import javax.inject.Inject

class SocialLoginRemoteDataSource @Inject constructor(val lovenParkService: LovenParkService) {

    suspend fun login(): TokenInfo {
        return lovenParkService.login()
    }
}