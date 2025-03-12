package com.schwarzit.lovenpark.mappin.data.remote

import com.schwarzit.lovenpark.mappin.data.local.FavouriteMapPinDao
import com.schwarzit.lovenpark.mappin.data.local.MapPinDAO
import com.schwarzit.lovenpark.mappin.data.local.MapPinsLocalDataSource
import com.schwarzit.lovenpark.mappin.data.local.toFavouriteMapPinRealm
import com.schwarzit.lovenpark.mappin.data.local.toMapPinUIModel
import com.schwarzit.lovenpark.mappin.domain.MapPinUIModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapPinsRepository @Inject constructor(
    private val remoteDataSource: MapPinsRemoteDataSource,
    private val localDataSource: MapPinsLocalDataSource
) {
    private val mapPinDAO: MapPinDAO = MapPinDAO()

    var cachedMapPins: List<MapPinUIModel> = emptyList()

    private val favouriteMapPinDao = FavouriteMapPinDao()

    /**
     * Returns the pins fetched from the server and stores them in the database
     */
    suspend fun getMapPins(language: String, forceFetch: Boolean): List<MapPinUIModel> {
        val fetchedMapPin: List<MapPinModel>?
        if (cachedMapPins.isEmpty() || forceFetch) {
            cachedMapPins = emptyList()
            fetchedMapPin = getRemoteMapPins(language)
            saveMapPinInDatabase(fetchedMapPin)
            cachedMapPins = fetchedMapPin?.map { it.toMapPinUIModel() } ?: emptyList()
        } else {
            return cachedMapPins
        }
        return fetchedMapPin?.map { it.toMapPinUIModel() } ?: emptyList()
    }

    fun getSavedMapPinsFromDatabase(): List<MapPinUIModel> = localDataSource.getLastPins()
        .map { it.toMapPinUIModel() }

    private suspend fun getRemoteMapPins(language: String): List<MapPinModel>? = remoteDataSource
        .getAllPointsOfInterest(language)
        .body()

    fun getFavouriteMapPinsIdsFromDatabase(): List<String> =
        localDataSource.getLastFavouritePins().map { it.id }

    fun saveMapPinInDatabase(fetchedMapPins: List<MapPinModel>?) {
        CoroutineScope(Dispatchers.IO).launch {
            fetchedMapPins?.forEach { mapPin ->
                if (mapPinDAO.getMapPinById(mapPin.id) == null) {
                    mapPinDAO.saveMapPin(mapPin.toMapPinRealm())
                }
            }
        }
    }

    fun updateFavouriteMapPinInDatabase(mapPinId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (favouriteMapPinDao.getFavouriteMapPinById(mapPinId) == null) {
                favouriteMapPinDao.saveFavouriteMapPinId(mapPinId.toFavouriteMapPinRealm())
            } else {
                favouriteMapPinDao.deleteFavouriteMapPinId(mapPinId.toFavouriteMapPinRealm())
            }
        }
    }
}