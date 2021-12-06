package com.example.harmanweatherapp.Services

import com.example.harmanweatherapp.Models.RealmCityModel
import io.realm.*
import kotlinx.coroutines.flow.Flow

class RealmService {

    companion object {
        val instance = RealmService()
    }

    lateinit var realm: Realm
    val config = RealmConfiguration.with(schema = setOf(RealmCityModel::class))

    init {
        realm = Realm.open(config)
    }

    fun fetchAllCities(): List<RealmCityModel> {
        return realm.objects(RealmCityModel::class).sortedBy { it.name }
    }

    fun isCityStored(name: String): Boolean {
        return realm.objects<RealmCityModel>().query("name == $0", name).count() != 0
    }

    fun getLocationNames(): List<String> {
        val models = realm.objects<RealmCityModel>().query()
        val names = models.map { it.name }
        return names
    }

    fun getAllCities(): List<RealmCityModel> {
        return realm.objects<RealmCityModel>().query()
    }

    fun addCityToDB(city: RealmCityModel) {
        realm.writeBlocking {
            copyToRealm(city)
        }
    }

    fun deleteAllCities() {
        realm.writeBlocking {
            objects<RealmCityModel>().query().delete()
        }
    }

    fun deleteCity(title: String) {
        realm.writeBlocking {
            objects<RealmCityModel>().query("name = $0", title)
                .first()
                .let { findLatest(it) }
                ?.delete()
                ?: throw IllegalStateException("City not found.")
        }
    }

    fun updateLocationWeather(name: String, model: RealmCityModel) {
        realm.writeBlocking {
            val realmModel =
                realm.objects<RealmCityModel>().query("name == $0", name).firstOrNull()
                    ?: return@writeBlocking
            findLatest(realmModel)?.update(model)
        }
    }

    fun observeChanges(): Flow<List<RealmCityModel>> {
        return realm.objects(RealmCityModel::class).observe()
    }

}