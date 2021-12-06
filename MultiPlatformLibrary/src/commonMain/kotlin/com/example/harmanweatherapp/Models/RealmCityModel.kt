package com.example.harmanweatherapp.Models

import io.realm.RealmObject

class RealmCityModel: RealmObject {
    var name: String = ""
    var temp: Double = 0.0
    var sunrise: Long = 0
    var sunset: Long = 0
    var feelsLike: Double = 0.0
    var tempMin: Double = 0.0
    var tempMax: Double = 0.0
    var pressure: Long = 0
    var humidity: Long = 0
    var main: String = ""
    var lat: Double = 0.0
    var lon: Double = 0.0

    fun update(model: RealmCityModel) {
        this.name = model.name
        this.feelsLike = model.feelsLike
        this.temp = model.temp
        this.tempMax = model.tempMax
        this.tempMin = model.tempMin
        this.pressure = model.pressure
        this.humidity = model.humidity
        this.main = model.main
        this.lat = model.lat
        this.lon = model.lon
        this.sunrise = model.sunrise
        this.sunset = model.sunset
    }

}