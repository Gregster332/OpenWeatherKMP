package com.example.harmanweatherapp.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Welcome(
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val sys: Sys,
    val name: String,
)

@Serializable
data class Coord (
    val lon: Double,
    val lat: Double
)

@Serializable
data class Main (
    val temp: Double,

    @SerialName("feels_like")
    val feelsLike: Double,

    @SerialName("temp_min")
    val tempMin: Double,

    @SerialName("temp_max")
    val tempMax: Double,

    val pressure: Long,
    val humidity: Long
)

@Serializable
data class Sys (
    val sunrise: Long,
    val sunset: Long
)

@Serializable
data class Weather (
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)
