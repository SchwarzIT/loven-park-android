package com.schwarzit.lovenpark.network
import com.schwarzit.lovenpark.user.RefreshTokenInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface RefreshTokenService {
    @GET("api/v1/users/token/refresh")
    suspend fun getToken(
        @Header("Authorization") refreshToken: String
    ) : Response<RefreshTokenInfo>
}