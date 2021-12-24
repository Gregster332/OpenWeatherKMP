package harmanweatherapp

import com.example.harmanweatherapp.Models.*
import com.example.harmanweatherapp.Services.HttpClientProtocol
import com.example.harmanweatherapp.Services.NetworkInterface
import com.example.harmanweatherapp.Services.NetworkService
import io.realm.internal.platform.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

private fun getWelcome() = Welcome(Coord(0.0, 0.0),
    listOf(Weather(0L, "", "", "")),
    Main(0.0, 0.0, 0.0, 0.0, 0L, 0L),
    Sys(0L, 0L),
    "Moscow"
)

private fun getWelcomeWithError() = Welcome(Coord(0.0, 0.0),
    listOf(Weather(0L, "", "", "")),
    Main(0.0, 0.0, 0.0, 0.0, 0L, 0L),
    Sys(0L, 0L),
    "NULL"
)


//class NetworkServiceFake: NetworkInterface {
//    override suspend fun getDataByCityName(cityName: String): Welcome {
//       return getWelcome()
//    }
//
//    override suspend fun getDataByCoordinates(lat: Double, lon: Double): Welcome {
//        return getWelcome()
//    }
//}

class HttpClientFake {
    fun get() = getWelcome()
    fun getWithError() = getWelcomeWithError()
}

class NetworkServiceFake(override val httpClient: HttpClientFake) : HttpClientProtocol<HttpClientFake>, NetworkInterface {
    override suspend fun getDataByCityName(cityName: String): Welcome {
        if (cityName != "") {
            return httpClient.get()
        } else {
            return httpClient.getWithError()
        }
    }

    override suspend fun getDataByCoordinates(lat: Double, lon: Double): Welcome {
        if (lat != 0.0 && lon != 0.0) {
            return httpClient.get()
        } else {
            return httpClient.getWithError()
        }
    }
}

class FirstTest {

    private val httpClientFake = HttpClientFake()
    lateinit var service: NetworkInterface

    @Test
    fun check_get_data_by_city_name_works() = runBlocking {
        service = NetworkServiceFake(httpClientFake)
        val result = service.getDataByCityName("Moscow")
        assertTrue { result.name == "Moscow" }
    }

    @Test
    fun check_get_data_by_coordinates_work() = runBlocking {
        service = NetworkServiceFake(httpClientFake)
        val result = service.getDataByCoordinates(1.0, 1.0)
        assertTrue { result.name == "Moscow" }
    }

    @Test
    fun check_get_data_by_coordinates_work_with_error() = runBlocking {
        service = NetworkServiceFake(httpClientFake)
        val result = service.getDataByCoordinates(0.0, 0.0)
        assertTrue { result.name == "NULL" }
    }

    @Test
    fun check_get_data_by_city_name_works_with_error() = runBlocking {
        service = NetworkServiceFake(httpClientFake)
        val result = service.getDataByCityName("")
        assertTrue { result.name == "NULL" }
    }

}