package com.schwarzit.lovenpark.network

import android.content.Context
import android.util.Log
import com.schwarzit.lovenpark.BuildConfig
import com.schwarzit.lovenpark.keyStore.KeyStoreManager
import com.schwarzit.lovenpark.keyStore.KeyStoreSharedPrefsKeys
import com.schwarzit.lovenpark.keyStore.KeyStoreSharedPrefsKeys.Companion.ACCESS_TOKEN_PAIR_FIRST_KEY
import com.schwarzit.lovenpark.keyStore.KeyStoreSharedPrefsKeys.Companion.ACCESS_TOKEN_PAIR_SECOND_KEY
import com.schwarzit.lovenpark.keyStore.KeyStoreSharedPrefsKeys.Companion.ID_TOKEN_TOKEN_PAIR_FIRST_KEY
import com.schwarzit.lovenpark.keyStore.KeyStoreSharedPrefsKeys.Companion.ID_TOKEN_TOKEN_PAIR_SECOND_KEY
import com.schwarzit.lovenpark.user.google.GoogleSignInRepository
import com.schwarzit.lovenpark.user.socialLogin.SocialUserSharedPrefs
import com.schwarzit.lovenpark.utils.Util.Companion.BEARER_TOKEN
import com.schwarzit.lovenpark.utils.Util.Companion.URL_POI_DEBUG
import com.schwarzit.lovenpark.utils.Util.Companion.URL_POI_PROD
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class LovenParkInterceptor @Inject constructor(
    val context: Context,
    private val refreshApiClient: RefreshTokenService,
    val keyStoreManager: KeyStoreManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val isUserLogged = !SocialUserSharedPrefs.getLoggedUser(context).isNullOrBlank()
        val getPoiUrl = if (BuildConfig.DEBUG) URL(URL_POI_DEBUG).toHttpUrlOrNull()
        else URL(URL_POI_PROD).toHttpUrlOrNull()
        val response: Response
        val idToken = keyStoreManager.decryptToken(
            ID_TOKEN_TOKEN_PAIR_FIRST_KEY,
            ID_TOKEN_TOKEN_PAIR_SECOND_KEY
        )

        if (isUserLogged) {
            if (!request.url.toString().contains("api/v1/poi?language")) {
                val accessToken = keyStoreManager.decryptToken(
                    ACCESS_TOKEN_PAIR_FIRST_KEY,
                    ACCESS_TOKEN_PAIR_SECOND_KEY
                )
                response = chain.proceed(newRequestWithToken(accessToken, request))

                if (response.code == HttpURLConnection.HTTP_FORBIDDEN) {
                    val newToken = refreshAccessToken(refreshApiClient)

                    if (newToken.isNullOrBlank()) {
                        context.let {
                            SocialUserSharedPrefs.removeUser(it)
                            GoogleSignInRepository(it).signOut()
                        }
                        return response
                    }
                    response.close()
                    return chain.proceed(newRequestWithToken(newToken, request))
                }

                if (response.code == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
                    context.let {
                        SocialUserSharedPrefs.removeUser(it)
                        GoogleSignInRepository(it).signOut()
                    }
                    return response
                }
                return response
            }

        } else {
            if (request.url != getPoiUrl) {
                GoogleSignInRepository(context).getAccount(context)
                return chain.proceed(newRequestWithToken(idToken, request))
            }
        }
        return chain.proceed(request)
    }

    private fun newRequestWithToken(token: String?, request: Request): Request =
        request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

    private fun refreshAccessToken(
        refreshApiClient: RefreshTokenService
    ): String? {
        val refreshToken = BEARER_TOKEN + keyStoreManager.decryptToken(
            KeyStoreSharedPrefsKeys.REFRESH_TOKEN_PAIR_FIRST_KEY,
            KeyStoreSharedPrefsKeys.REFRESH_TOKEN_PAIR_SECOND_KEY
        )
        var token: String? = null
        try {
            val response = runBlocking { refreshApiClient.getToken(refreshToken) }
            token = response.body()?.accessToken

            token?.let {
                keyStoreManager.clearToken(
                    ACCESS_TOKEN_PAIR_FIRST_KEY,
                    ACCESS_TOKEN_PAIR_SECOND_KEY
                )
                keyStoreManager.encryptToken(
                    token,
                    ACCESS_TOKEN_PAIR_FIRST_KEY,
                    ACCESS_TOKEN_PAIR_SECOND_KEY
                )
                return keyStoreManager.decryptToken(
                    ACCESS_TOKEN_PAIR_FIRST_KEY,
                    ACCESS_TOKEN_PAIR_SECOND_KEY
                )

            }
        } catch (e: Exception) {
            e.message?.let { Log.e("Exception_", it) }
        }
        return token
    }

}
