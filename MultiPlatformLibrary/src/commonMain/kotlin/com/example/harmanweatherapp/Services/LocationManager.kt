package com.example.harmanweatherapp.Services

import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.readOnly
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

//class LocationManager(var locTracker: LocationTracker): ViewModel() {
//
//    private val _lat: MutableLiveData<Double> = MutableLiveData(0.0)
//    val lat: LiveData<Double> = _lat.readOnly()
//
//    private val _lon: MutableLiveData<Double> = MutableLiveData(0.0)
//    val lon: LiveData<Double> = _lon.readOnly()
//
//    init {
//        viewModelScope.launch {
//            //locTracker = LocationTracker()
//            locTracker.getLocationsFlow()
//                .distinctUntilChanged()
//                .collect {
//                    _lat.value = it.latitude
//                    _lon.value = it.longitude
//                }
//        }
//    }
//
//    fun onStartTracking() {
//     viewModelScope.launch { locTracker.startTracking() }
//    }
//
//    fun onStopTracking() {
//        viewModelScope.launch { locTracker.stopTracking() }
//    }
//
//}