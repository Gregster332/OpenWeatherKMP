package harmanweatherapp

import com.example.harmanweatherapp.Models.RealmCityModel
import com.example.harmanweatherapp.Services.RealmProtocol
import com.example.harmanweatherapp.Services.RealmServiceProtocol
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun getRealmCityObject(update: Boolean = false, forAdding: Boolean = false): RealmCityModel {
    val city = RealmCityModel()
    if (forAdding) {
        city.name = "Ufa"
    } else {
        city.name = "Moscow"
    }
    if (update) {
        city.feelsLike = 1.0
    } else {
        city.feelsLike = 0.0
    }
    city.temp = 0.0
    city.tempMin = 0.0
    city.tempMax = 0.0
    city.pressure = 0L
    city.humidity = 0L
    city.lat = 0.0
    city.lon = 0.0
    city.sunrise = 0L
    city.sunset = 0L
    return city
}

class RealmFake {

    var dataList: List<RealmCityModel> = listOf(getRealmCityObject())

    fun objects(): List<RealmCityModel> {
        return dataList
    }

    fun copyToRealm(city: RealmCityModel) {
        val modifiedList = dataList + city
        dataList = modifiedList
    }
}

class RealmServiceFake: RealmServiceProtocol {

    val realmFake = RealmFake()

    override fun fetchAllCities(): List<RealmCityModel> {
      return realmFake.objects()
    }

    override fun isCityStored(name: String): Boolean {
       return true
    }

    override fun getLocationNames(): List<String> {
        return listOf<String>("Moscow")
    }

    override fun getAllCities(): List<RealmCityModel> {
        return realmFake.objects()
    }

    override fun addCityToDB(city: RealmCityModel) {
        realmFake.copyToRealm(city)
    }

    override fun deleteAllCities() {
        realmFake.dataList.toMutableList().clear()
    }

    override fun deleteCity(title: String) {
        for (city in realmFake.dataList) {
            if (city.name == title) {
                realmFake.dataList.toMutableList().remove(city)
            }
        }
    }

    override fun updateLocationWeather(name: String, model: RealmCityModel) {
        realmFake.dataList.toMutableList().clear()
        realmFake.copyToRealm(getRealmCityObject(update = true))
    }

    override fun observeChanges(): Flow<List<RealmCityModel>> {
        TODO("Not yet implemented")
    }

}

class RealmServiceTests {

    private val realmFake = RealmFake()
    private val realmServiceFake: RealmServiceProtocol = RealmServiceFake()

    @Test
    fun check_fetch_all_cities_work() {
        val cities = realmServiceFake.fetchAllCities()
        assertTrue { !cities.isEmpty() }
        assertEquals(cities[0].name, "Moscow")
    }

    @Test
    fun check_get_location_names_work() {
        val citiesNames = realmServiceFake.getLocationNames()
        assertTrue { !citiesNames.isEmpty() }
        assertEquals(citiesNames[0], "Moscow")
    }

    @Test
    fun check_add_city_to_db_work() {
        realmServiceFake.addCityToDB(getRealmCityObject(forAdding = true))
        val cities = realmServiceFake.fetchAllCities()
        assertTrue { !cities.isEmpty() }
        assertTrue { cities.count() == 2 }
    }

    @Test
    fun check_delete_city_from_db_works() {
        realmServiceFake.deleteCity("Ufa")
        val cities = realmServiceFake.fetchAllCities()
        assertTrue { cities.count() == 1 }
    }

}