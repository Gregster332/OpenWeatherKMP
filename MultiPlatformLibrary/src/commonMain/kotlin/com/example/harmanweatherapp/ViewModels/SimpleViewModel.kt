package com.example.harmanweatherapp.ViewModels

import com.example.harmanweatherapp.Models.*
import com.example.harmanweatherapp.Services.NetworkService
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.viewmodel.ViewModel
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

    val networkService = NetworkService.instance

    val cities = listOf("Moscow", "Samara", "Huston", "Arizona", "Ufa")

    fun onCounterButtonPressed(name: String) {
        getCityByName(name = name) {
            val current = _counter.value
            _counter.value = it!!
            //print(_counter.value)
        }
    }

    fun getCityByName(name: String, callback: (Welcome?) -> Unit) {
        MainScope().launch {
            networkService.getDataByCityName(name, callback = { result ->
                if (result != null) {
                    print(result)
                    callback(result)
                }
            }, failure = { error ->
                if (error != null) {
                    print(error.message)
                }
            })
        }
    }

}