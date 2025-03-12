package com.schwarzit.lovenpark.di

import android.content.Context
import com.schwarzit.lovenpark.CoreConfig
import com.schwarzit.lovenpark.camera.BitmapProcessor
import com.schwarzit.lovenpark.dialogs.DialogProvider
import com.schwarzit.lovenpark.keyStore.KeyStoreManager
import com.schwarzit.lovenpark.mappin.data.local.MapPinsLocalDataSource
import com.schwarzit.lovenpark.mappin.data.remote.MapPinsRemoteDataSource
import com.schwarzit.lovenpark.network.LovenParkApiClient
import com.schwarzit.lovenpark.network.LovenParkService
import com.schwarzit.lovenpark.network.RefreshTokenApiClient
import com.schwarzit.lovenpark.network.RefreshTokenService
import com.schwarzit.lovenpark.profile.data.UserProfileRepository
import com.schwarzit.lovenpark.profile.data.remote.UserProfileRemoteDataSource
import com.schwarzit.lovenpark.profile.signals.data.local.SignalsLocalDataSource
import com.schwarzit.lovenpark.profile.signals.data.remote.SignalsRemoteDataSource
import com.schwarzit.lovenpark.signal.SignalRemoteData
import com.schwarzit.lovenpark.signal.StringProvider
import com.schwarzit.lovenpark.user.socialLogin.SocialLoginLocalDataSource
import com.schwarzit.lovenpark.user.socialLogin.SocialLoginRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideApiClient(@ApplicationContext appContext: Context, refreshTokenApiClient: RefreshTokenService, keyStoreManager: KeyStoreManager) =
        LovenParkApiClient.getClient(appContext, refreshTokenApiClient, keyStoreManager)

    @Provides
    @Singleton
    fun provideRefreshApiClient() = RefreshTokenApiClient.getClient()

    @Provides
    @Singleton
    fun provideSignalRemoteDataSource(apiClientLoven: LovenParkService):
            SignalRemoteData = SignalRemoteData(apiClientLoven)

    @Provides
    @Singleton
    fun provideMapPinRemoteDataSource(apiClientLoven: LovenParkService): MapPinsRemoteDataSource =
        MapPinsRemoteDataSource(apiClientLoven)

    @Provides
    @Singleton
    fun provideMapPinLocalDataSource(): MapPinsLocalDataSource =
        MapPinsLocalDataSource()

    @Provides
    @Singleton
    fun provideSocialLoginRemoteDataSource(apiClientLoven: LovenParkService): SocialLoginRemoteDataSource =
        SocialLoginRemoteDataSource(apiClientLoven)

    @Provides
    @Singleton
    fun provideSocialLoginLocalDataSource(): SocialLoginLocalDataSource =
        SocialLoginLocalDataSource()

    @Provides
    @Singleton
    fun provideSignalsRemoteDataSource(apiClientLoven: LovenParkService): SignalsRemoteDataSource =
        SignalsRemoteDataSource(apiClientLoven)

    @Provides
    @Singleton
    fun provideSignalsLocalDataSource(userProfileRepository: UserProfileRepository): SignalsLocalDataSource =
        SignalsLocalDataSource(userProfileRepository)

    @Provides
    @Singleton
    fun provideUserProfileRemoteDataSource(apiClientLoven: LovenParkService): UserProfileRemoteDataSource =
        UserProfileRemoteDataSource(apiClientLoven)


    @Provides
    @Singleton
    fun provideKeyStoreManager(@ApplicationContext appContext: Context) =
        KeyStoreManager(appContext)

    @Provides
    @Singleton
    fun provideDialogProvider(@ApplicationContext appContext: Context) =
        DialogProvider(appContext)

    @Provides
    @Singleton
    fun provideBitmapProcessor(@ApplicationContext appContext: Context) =
        BitmapProcessor(appContext)

    @Provides
    @Singleton
    fun provideStringProvider(@ApplicationContext appContext: Context) =
        StringProvider(appContext)

    @Provides
    fun provideCoreConfig() =
        CoreConfig()
}