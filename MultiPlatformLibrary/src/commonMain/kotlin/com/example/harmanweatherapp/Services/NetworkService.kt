package com.example.harmanweatherapp.Services

import com.example.harmanweatherapp.Models.Welcome
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class NetworkService {

    companion object {
        val instance = NetworkService()
    }

    private val httpClient = HttpClient()



    suspend fun getDataByCityName(cityName: String): Welcome  {
        val url =
            "https://api.openweathermap.org/data/2.5/weather?q=${cityName}&appid=c302a638f8f46f6a557e143a3a483647"
        val obj = httpClient.get<String>(url)
        val json = kotlinx.serialization.json.Json {
            ignoreUnknownKeys = true
        }
        val city = json.decodeFromString<Welcome>(obj)
        return city
    }


    suspend fun getDataByCoordinates(lat: Double, lon: Double): Welcome {
            val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=c302a638f8f46f6a557e143a3a483647"
            val obj = httpClient.get<String>(url)
            val json = kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            }
            val city = json.decodeFromString<Welcome>(obj)
            return city
    }

}