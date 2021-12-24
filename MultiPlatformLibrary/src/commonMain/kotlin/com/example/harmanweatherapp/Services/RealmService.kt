package com.example.harmanweatherapp.Services

import com.example.harmanweatherapp.Models.RealmCityModel
import io.realm.*
import kotlinx.coroutines.flow.Flow

interface RealmServiceProtocol {
    fun fetchAllCities(): List<RealmCityModel>
    fun isCityStored(name: String): Boolean
    fun getLocationNames(): List<String>
    fun getAllCities(): List<RealmCityModel>
    fun addCityToDB(city: RealmCityModel)
    fun deleteAllCities()
    fun deleteCity(title: String)
    fun updateLocationWeather(name: String, model: RealmCityModel)
    fun observeChanges(): Flow<List<RealmCityModel>>
}

interface RealmProtocol<T> {
    var realm: T
}

class RealmService: RealmServiceProtocol, RealmProtocol<Realm> {

    companion object {
        val instance = RealmService()
    }

    override lateinit var realm: Realm
    val config = RealmConfiguration.with(schema = setOf(RealmCityModel::class))

    init {
        realm = Realm.open(config)
    }

    override fun fetchAllCities(): List<RealmCityModel> {
        return realm.objects(RealmCityModel::class).sortedBy { it.name }
    }

    override fun isCityStored(name: String): Boolean {
        return realm.objects<RealmCityModel>().query("name == $0", name).count() != 0
    }

    override fun getLocationNames(): List<String> {
        val models = realm.objects<RealmCityModel>().query()
        val names = models.map { it.name }
        return names
    }

    override fun getAllCities(): List<RealmCityModel> {
        return realm.objects<RealmCityModel>().query()
    }

    override fun addCityToDB(city: RealmCityModel) {
        realm.writeBlocking {
            copyToRealm(city)
        }
    }

    override fun deleteAllCities() {
        realm.writeBlocking {
            objects<RealmCityModel>().query().delete()
        }
    }

    override fun deleteCity(title: String) {
        realm.writeBlocking {
            objects<RealmCityModel>().query("name = $0", title)
                .first()
                .let { findLatest(it) }
                ?.delete()
                ?: throw IllegalStateException("City not found.")
        }
    }

    override fun updateLocationWeather(name: String, model: RealmCityModel) {
        realm.writeBlocking {
            val realmModel =
                realm.objects<RealmCityModel>().query("name == $0", name).firstOrNull()
                    ?: return@writeBlocking
            findLatest(realmModel)?.update(model)
        }
    }

    override fun observeChanges(): Flow<List<RealmCityModel>> {
        return realm.objects(RealmCityModel::class).observe()
    }

}