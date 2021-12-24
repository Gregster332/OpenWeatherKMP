package com.example.harmanweatherapp.Services

import com.example.harmanweatherapp.Models.Welcome
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

interface NetworkInterface {
    suspend fun getDataByCityName(cityName: String): Welcome
    suspend fun getDataByCoordinates(lat: Double, lon: Double): Welcome
}

interface HttpClientProtocol<T> {
    val httpClient: T
}

class NetworkService: HttpClientProtocol<HttpClient>, NetworkInterface {

    companion object {
        val instance = NetworkService()
        private val baseUrl = "https://api.openweathermap.org/data/2.5/weather?"
        private val apiKey = "c302a638f8f46f6a557e143a3a483647"
    }

    override val httpClient = HttpClient()

    override suspend fun getDataByCityName(cityName: String): Welcome  {
        val url = baseUrl + "q=${cityName}&appid=" + apiKey
        val obj = httpClient.get<String>(url)
        val json = kotlinx.serialization.json.Json {
            ignoreUnknownKeys = true
        }
        val city = json.decodeFromString<Welcome>(obj)
        return city
    }


    override suspend fun getDataByCoordinates(lat: Double, lon: Double): Welcome {
            val url = baseUrl + "lat=${lat}&lon=${lon}&appid=" + apiKey
            val obj = httpClient.get<String>(url)
            val json = kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            }
            val city = json.decodeFromString<Welcome>(obj)
            return city
    }
}