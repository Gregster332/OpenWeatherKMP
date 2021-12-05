package com.example.harmanweatherapp.ViewModels

import com.example.harmanweatherapp.Enums.LoadingState
import com.example.harmanweatherapp.Models.*
import com.example.harmanweatherapp.Services.NetworkService
import com.example.harmanweatherapp.Services.RealmService
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.livedata.*
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.util.reflect.*
import io.realm.internal.platform.runBlocking
import io.realm.objects
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

interface SimpleViewModeling {
    var cities: MutableList<RealmCityModel>
    //fun checkAndAddNewCity(name: String)
    fun checkAndAddNewCity(name: String, callback: (LoadingState) -> Unit)
    fun deleteCity(name: String)
    fun refresh(callback: () -> Unit)
}

class SimpleViewModel(val eventsDispatcher: EventsDispatcher<EventsListener>): SimpleViewModeling, ViewModel() {

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading.readOnly()

    private val _currentCity: MutableLiveData<Welcome> = MutableLiveData(Welcome(Coord(0.0, 0.0),
    listOf(Weather(0, "", "", "")),
    Main(0.0, 0.0, 0.0 , 0.0, 0, 0),
    Sys(0, 0),
    "None"))
    val currentCity: LiveData<Welcome> = _currentCity.readOnly()

    override var cities: MutableList<RealmCityModel> = mutableListOf()

    val realm = RealmService.instance
    val networkService = NetworkService.instance

//    init {
//        viewModelScope.async {
//            realm.realm.objects(RealmCityModel::class).observe().collect {
//                cities = it.toMutableList()
//                eventsDispatcher.dispatchEvent {
//                    isLoading(false)
//                    update()
//                }
//            }
//        }
//    }

    fun fetchAllCities() {
        cities = realm.fetchAllCities().sortedBy { it.name }.toMutableList()
    }

    fun convertFromWelcomeToRealmClass(welcome: Welcome): RealmCityModel {
        val city = RealmCityModel()
        city.name = welcome.name
        city.main = welcome.weather[0].main
        city.temp = welcome.main.temp
        city.tempMax = welcome.main.tempMax
        city.tempMin = welcome.main.tempMin
        city.feelsLike = welcome.main.feelsLike
        city.humidity = welcome.main.humidity
        city.pressure = welcome.main.pressure
        city.sunrise = welcome.sys.sunrise
        city.sunset = welcome.sys.sunset
        city.lon = welcome.coord.lon
        city.lat = welcome.coord.lat
        return city
    }

    fun fromRealmCityModelToWelcome(city: RealmCityModel): Welcome {
        var welcome = Welcome(name = city.name,
            coord = Coord(city.lon, city.lat),
            sys = Sys(city.sunrise, city.sunset),
            main = Main(city.temp, city.feelsLike, city.tempMin, city.tempMax, city.pressure, city.humidity),
            weather = listOf(Weather(0L, city.main, "", ""))
            )
        return welcome
    }


    interface EventsListener {
        fun update()
        fun isLoading(isLoading: Boolean)
        fun error(message: String)
    }

    fun getCurrentUserLocation(lat: Double, lon: Double, callback: (Welcome) -> Unit) {
        _loading.value = true
        viewModelScope.launch {
            kotlin.runCatching {
                networkService.getDataByCoordinates(lat, lon)
            }.onSuccess { result ->
                if (result != null) {
                    _loading.value = false
                   callback.invoke(result)
                }
            }.onFailure {
                _loading.value = false
            }
        }
    }

    override fun checkAndAddNewCity(name: String, callback: (LoadingState) -> Unit) {
        eventsDispatcher.dispatchEvent { isLoading(true) }
        _loading.value = true
        if (name.isEmpty()) {
            eventsDispatcher.dispatchEvent {
                isLoading(false)
                error("Epty name field")
            }
            _loading.value = false
            //callback.invoke(LoadingState.error)
            return
        }

        if (realm.isCityStored(name)) {
            eventsDispatcher.dispatchEvent {
                isLoading(false)
                error("Repeating location")
            }
            _loading.value = false
            //callback.invoke(LoadingState.error)
            return
        }

        viewModelScope.launch {
            kotlin.runCatching {
                networkService.getDataByCityName(name)
            }.onSuccess { result ->
                if (result != null) {
                    realm.addCityToDB(convertFromWelcomeToRealmClass(result))
                    eventsDispatcher.dispatchEvent {
                        isLoading(false)
                        update()
                    }
                    _loading.value = false
                    callback.invoke(LoadingState.success)
                }
            }.onFailure {
                eventsDispatcher.dispatchEvent {
                    isLoading(false)
                    error(it)
                }
                _loading.value = false
                callback.invoke(LoadingState.error)
            }
        }
    }

    override fun deleteCity(name: String) {
        realm.deleteCity(name)
    }

    override fun refresh(callback: () -> Unit) {
        _loading.value = true
        var counter = 0
       eventsDispatcher.dispatchEvent {
           isLoading(true)
       }

        val names = realm.getLocationNames()
        if (names.isEmpty()) {
            return
        }
        for (name in names) {
         viewModelScope.launch {
             kotlin.runCatching {
                 networkService.getDataByCityName(name)
             }.onSuccess {  result ->
                 
                 val response = networkService.getDataByCityName(name)
                 val model = convertFromWelcomeToRealmClass(response)
                 MainScope().async {
                     realm.updateLocationWeather(name, model)
                     counter += 1

                     if (counter == names.count()) {
                         _loading.value = false
                         callback.invoke()
                     }
                     eventsDispatcher.dispatchEvent {
                         isLoading(false)
                         update()
                     }
                 }

             }.onFailure {
                 _loading.value = false
              eventsDispatcher.dispatchEvent {
                  isLoading(false)
                  error(it)
              }
             }
         }

        }
    }

}