package com.schwarzit.lovenpark.profile.signals.data

import android.content.Context
import android.util.Log
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalModel
import com.schwarzit.lovenpark.profile.signals.data.local.SignalDAO
import com.schwarzit.lovenpark.profile.signals.data.local.SignalsLocalDataSource
import com.schwarzit.lovenpark.profile.signals.data.local.toSignalModel
import com.schwarzit.lovenpark.profile.signals.data.remote.SignalRemoteModel
import com.schwarzit.lovenpark.profile.signals.data.remote.SignalsRemoteDataSource
import com.schwarzit.lovenpark.profile.signals.data.remote.toSignalModel
import com.schwarzit.lovenpark.profile.signals.data.remote.toSignalRealm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignalsRepository @Inject constructor(
    private val localDataSource: SignalsLocalDataSource,
    private val remoteDataSource: SignalsRemoteDataSource
) {
    private val signalDAO: SignalDAO = SignalDAO()

    private var cachedActiveSignals: List<SignalModel> = emptyList()
    private var cachedUserSignals: List<SignalModel> = emptyList()
    private var cachedOthersSignals: List<SignalModel> = emptyList()

    /**
     * Returns the active signals fetched from the server and stores them in the database
     */
    suspend fun getActiveSignals(): List<SignalModel> {
        val fetchedActiveSignals: List<SignalRemoteModel>
        if (cachedActiveSignals.isEmpty()) {
            fetchedActiveSignals = getRemoteActiveSignals()
            saveSignalInDatabase(fetchedActiveSignals)
            cachedActiveSignals = fetchedActiveSignals.map { it.toSignalModel() }
        } else {
            return cachedActiveSignals
        }
        return fetchedActiveSignals.map { it.toSignalModel() }
    }

    /**
     * Returns the user signals fetched from the server and stores them in the database
     */
    suspend fun getUserSignals(): List<SignalModel> {
        val fetchedUserSignals: List<SignalRemoteModel>
        if (cachedUserSignals.isEmpty()) {
            fetchedUserSignals = getRemoteUserSignals()
            saveSignalInDatabase(fetchedUserSignals)
            cachedUserSignals = fetchedUserSignals.map { it.toSignalModel() }
        } else {
            return cachedActiveSignals
        }
        return fetchedUserSignals.map { it.toSignalModel() }
    }

    /**
     * Returns the others' signals fetched from the server and stores them in the database
     */
    suspend fun getOthersSignals(): List<SignalModel> {
        val fetchedOthersSignals: List<SignalRemoteModel>
        if (cachedOthersSignals.isEmpty()) {
            fetchedOthersSignals = getRemoteOthersSignals()
            saveSignalInDatabase(fetchedOthersSignals)
            cachedUserSignals = fetchedOthersSignals.map { it.toSignalModel() }
        } else {
            return cachedActiveSignals
        }
        return fetchedOthersSignals.map { it.toSignalModel() }
    }

    fun getSavedActiveSignalsFromDatabase(): List<SignalModel> =
        localDataSource.getLastActiveSignals()
            .map { it.toSignalModel() }

    fun getSavedUserSignalsFromDatabase(context: Context): List<SignalModel> =
        localDataSource.getLastUserSignals(context)
            .map { it.toSignalModel() }


    fun getSavedOthersSignalsFromDatabase(context: Context): List<SignalModel> =
        localDataSource.getLastOthersSignals(context)
            .map { it.toSignalModel() }

    fun getSavedSignalFromDatabase(signalId: String): SignalModel? =
        getSavedActiveSignalsFromDatabase().firstOrNull { it.id == signalId }

    private suspend fun getRemoteActiveSignals(): List<SignalRemoteModel> =
        remoteDataSource.getAllActiveSignals()

    suspend fun getRemoteUserSignals(): List<SignalRemoteModel> =
        remoteDataSource.getAllUserSignals()

    suspend fun getRemoteOthersSignals(): List<SignalRemoteModel> =
        remoteDataSource.getAllOthersSignals()

    suspend fun getRemoteSignal(signalId: String): SignalModel? =
        getActiveSignals().firstOrNull { it.id == signalId }

    private fun saveSignalInDatabase(fetchedActiveSignals: List<SignalRemoteModel>?) {
        CoroutineScope(Dispatchers.IO).launch {
            fetchedActiveSignals?.forEach { activeSignal ->
                if (signalDAO.getSignalById(activeSignal.id) == null) {
                    signalDAO.saveSignal(activeSignal.toSignalRealm())
                }
            }
        }
    }
}