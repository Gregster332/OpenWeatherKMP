package com.example.harmanweatherapp.ViewModels

import com.example.harmanweatherapp.Models.*
import com.example.harmanweatherapp.Services.NetworkService
import com.example.harmanweatherapp.Services.RealmService
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.livedata.setValue
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.util.reflect.*
import io.realm.objects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SimpleViewModel() : ViewModel() {
    private val _counter: MutableLiveData<Welcome> = MutableLiveData(Welcome(coord = Coord(0.0, 0.0),
        name = "NONE",
        main = Main(0.0, 0.0, 0.0, 0.0, 0L, 0L),
        sys = Sys(0L, 0L),
        weather = listOf(Weather(0L, "", "", ""))
    ))
    val counter: LiveData<Welcome> = _counter
    private val _error: MutableLiveData<Boolean> = MutableLiveData(false)
    val error: LiveData<Boolean> = _error

    val networkService = NetworkService.instance
    val realm = RealmService.instance

    fun fetchAllCities(): List<RealmCityModel> {
        return realm.realm.objects(RealmCityModel::class).sortedBy { it.name }
    }

    fun deleteAllCities() {
        realm.deleteAllCities()
    }
    
    fun deleteCity(name: String) {
        realm.deleteCity(name)
    }

    fun addCityToDB(name: String) {
        getCityByName(name = name) {
            if (it != null) {
                viewModelScope.launch {
                    if (counter.value.name != "NONE" ) {
                        realm.addCityToDB(convertFromWelcomeToRealmClass(counter.value))
                    }
                }
            } else {
                _error.setValue(true, true)
            }
        }
    }

    private fun getCityByName(name: String, callback: (Welcome?) -> Unit) {
        MainScope().launch {
            networkService.getDataByCityName(name, callback = { result ->
                if (result != null) {
                    _counter.postValue(result)
                    callback.invoke(result)
                }
            }, failure = { error ->
                if (error != null) {
                    _error.postValue(true)
                }
            })
        }
    }

    fun refreshCities() {

            val names: MutableList<String> = mutableListOf()
            val results = fetchAllCities()
            for (city in results) {
                names.add(city.name)
            }
            deleteAllCities()
        viewModelScope.launch {
            for (name in names) {
                getCityByName(name, callback = {
                    if (it != null) {
                        print(it)
                        realm.addCityToDB(convertFromWelcomeToRealmClass(it))
                    } else {
                        _error.setValue(true, false)
                    }
                })
            }
        }
    }

    fun getCityByCoordinates() {

    }

    private fun convertFromWelcomeToRealmClass(welcome: Welcome): RealmCityModel {
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

    fun doErrorFalse() {
        _error.setValue(false, true)
    }

}