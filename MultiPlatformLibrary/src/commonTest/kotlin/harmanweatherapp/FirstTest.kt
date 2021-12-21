package harmanweatherapp

import com.example.harmanweatherapp.Models.*
import com.example.harmanweatherapp.Services.NetworkService
import com.example.harmanweatherapp.Services.RealmService
import com.example.harmanweatherapp.ViewModels.SimpleViewModel
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.realm.internal.platform.runBlocking

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class FirstTest {

    @MockK
    lateinit var manager: NetworkService

    private fun getWelcome() = Welcome(Coord(0.0, 0.0), listOf(Weather(0L, "", "", "")),
    Main(0.0, 0.0, 0.0, 0.0, 0L, 0L),
    Sys(0L, 0L),
        "NONE"
    )

    @BeforeTest
    fun beforeTest() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        //coEvery { manager.getDataByCityName("") } returns
        coEvery { manager.getDataByCityName("") } returns getWelcome()
        coEvery {manager.getDataByCoordinates(0.0, 0.0)} returns getWelcome()
    }

   @Test
   fun test_one() = runBlocking {
      val result = manager.getDataByCityName("")
       assertTrue { result == getWelcome() }
//       val b = 0
//       assertTrue { b == 0 }
   }

    @Test
    fun test_second() = runBlocking {
        val result = manager.getDataByCoordinates(0.0, 0.0)
        assertTrue { result.name == "NONE" }
    }
}