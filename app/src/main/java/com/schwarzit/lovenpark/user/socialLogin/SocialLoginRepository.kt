package com.schwarzit.lovenpark.user.socialLogin

import com.schwarzit.lovenpark.user.TokenInfo
import javax.inject.Inject

class SocialLoginRepository @Inject constructor(
    private val socialLoginRemoteDataSource: SocialLoginRemoteDataSource,
    private val socialLoginLocalDataSource: SocialLoginLocalDataSource
) {

    suspend fun loginUser(): TokenInfo {
        return socialLoginRemoteDataSource.login()
    }
}