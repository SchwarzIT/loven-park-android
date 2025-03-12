package com.schwarzit.lovenpark.mappin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarzit.lovenpark.mappin.data.remote.MapPinsRepository
import com.schwarzit.lovenpark.mappin.domain.MapPinUIModel
import com.schwarzit.lovenpark.mappin.domain.PinCategory
import com.schwarzit.lovenpark.mappin.domain.toMapPinModel
import com.schwarzit.lovenpark.profile.data.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapPinsViewModel @Inject constructor(
    private val repository: MapPinsRepository,
    private val repositoryUser: UserProfileRepository
) : ViewModel() {

    val liveDataMapPins = MutableLiveData<List<MapPinUIModel>>()

    private var _selectedPinCategories: MutableLiveData<List<PinCategory>> =
        MutableLiveData(emptyList())
    val selectedPinCategories: LiveData<List<PinCategory>> = _selectedPinCategories

    private var _selectedPin: MutableLiveData<MapPinUIModel> = MutableLiveData()
    val selectedPin: LiveData<MapPinUIModel> = _selectedPin

    fun getUserWhenOnline() {
        try {
            viewModelScope.launch {
                repositoryUser.getUser()
            }
        } catch (e: Exception) {
            Log.e("request_", "${e.message}")
        }
    }

    fun updateSelectedCategories(category: PinCategory) {
        _selectedPinCategories.value?.toMutableList()?.let { list ->
            if (list.contains(category)) {
                list.remove(category)
            } else {
                list.add(category)
            }
            _selectedPinCategories.postValue(list)
        }
    }

    fun requestMapPinsWhenOnline(language: String, forceFetch: Boolean) {
        viewModelScope.launch {
            liveDataMapPins.postValue(repository.getMapPins(language, forceFetch))
        }
    }

    fun requestMapPinsWhenOffline() {
        liveDataMapPins.postValue(repository.getSavedMapPinsFromDatabase())
    }

    fun saveMapPinInDatabase(mapPins: List<MapPinUIModel>) {
        mapPins.map {
            it.toMapPinModel()
        }.let {
            repository.saveMapPinInDatabase(it)
        }
    }

    fun getFavouriteMapPinsFromDatabase() = repository.getFavouriteMapPinsIdsFromDatabase()

    fun updateFavouriteMapPinInDatabase(mapPinId: String) {
        repository.updateFavouriteMapPinInDatabase(mapPinId)
    }

    fun filterPinsFromSelectedCategories() =
        if (selectedPinCategories.value?.isEmpty() == true) {
            liveDataMapPins.value
        } else {
            liveDataMapPins.value?.filter {
                selectedPinCategories.value?.contains(it.category) == true
            }
        }

    fun saveLastSelectedMapPin(selectedPin: MapPinUIModel) {
        _selectedPin.value = selectedPin
    }
}