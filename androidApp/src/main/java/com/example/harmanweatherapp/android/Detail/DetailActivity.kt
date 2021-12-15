package com.example.harmanweatherapp.android.Detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.harmanweatherapp.Models.Welcome
import com.example.harmanweatherapp.android.R
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    var welcome: String? = null
    val format = SimpleDateFormat("HH:mm", Locale.ROOT)

    lateinit var tv1: TextView
    lateinit var tv2: TextView
    lateinit var tv3: TextView
    lateinit var pressure: TextView
    lateinit var humidity: TextView
    lateinit var sunset: TextView
    lateinit var sunrise: TextView
    lateinit var max: TextView
    lateinit var min: TextView
    lateinit var feelsLike: TextView
    lateinit var pressureText: TextView
    lateinit var humidityText: TextView
    lateinit var sunsetText: TextView
    lateinit var sunriseText: TextView
    lateinit var maxText: TextView
    lateinit var minText: TextView
    lateinit var feelsLikeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        tv1 = findViewById(R.id.cityName)
        tv2 = findViewById(R.id.description)
        tv3 = findViewById(R.id.temperatureText)
        pressure = findViewById(R.id.pressure)
        humidity = findViewById(R.id.humidity)
        sunset = findViewById(R.id.sunset)
        sunrise = findViewById(R.id.sunrise)
        max = findViewById(R.id.max)
        min = findViewById(R.id.min)

        pressureText = findViewById(R.id.pressureText)
        humidityText = findViewById(R.id.humidityText)
        sunsetText = findViewById(R.id.sunsetText)
        sunriseText = findViewById(R.id.sunriseText)
        maxText = findViewById(R.id.tempMaxText)
        minText = findViewById(R.id.tempMinText)
        feelsLikeText = findViewById(R.id.flText)

        feelsLike = findViewById(R.id.fl)
        var button = findViewById<Button>(R.id.back)

        val name = intent.getStringExtra("name")
        val desc = intent.getStringExtra("desc")
        val temp = intent.getStringExtra("temp")
        val humidityStr = intent.getStringExtra("hum")
        val pressureStr = intent.getStringExtra("pres")
        val sunsetStr = intent.getStringExtra("sunset")
        val sunriseStr = intent.getStringExtra("sunrise")
        val maxStr = intent.getStringExtra("max")
        val minStr = intent.getStringExtra("min")
        val fl = intent.getStringExtra("fl")

        humidityText.setText(R.string.humidity)
        pressureText.setText(R.string.pressure)
        sunsetText.setText(R.string.sset)
        sunriseText.setText(R.string.sise)
        maxText.setText(R.string.t_mx)
        minText.setText(R.string.t_mn)
        feelsLikeText.setText(R.string.fl)

        tv1.text = name
        setCurrentLanguageLabel(desc!!)
        tv3.text = "${temp}ºC"
        humidity.text = "$humidityStr %"
        pressure.text = "$pressureStr hPh"
        sunset.text = "${format.format(Date(sunsetStr!!.toLong()))}"
        sunrise.text = "${format.format(Date(sunriseStr!!.toLong()))}"
        max.text = "$maxStr ºC"
        min.text = "$minStr ºC"
        feelsLike.text = "$fl ºC"

        button.setOnClickListener {
            finish()
        }

    }

    private fun setCurrentLanguageLabel(text: String) {
        when(text) {
            "Clear" -> tv2.setText(R.string.clear)
            "Clouds" -> tv2.setText(R.string.clouds)
            "Mist" -> tv2.setText(R.string.mist)
            "Smoke" -> tv2.setText(R.string.smoke)
            "Haze" -> tv2.setText(R.string.haze)
            "Dust" -> tv2.setText(R.string.dust)
            "Thunderstorm" -> tv2.setText(R.string.thunderstorm)
            "Fog" -> tv2.setText(R.string.fog)
            "Sand" -> tv2.setText(R.string.sand)
            "Ash" -> tv2.setText(R.string.ash)
            "Squall" -> tv2.setText(R.string.squall)
            "Drizzle" -> tv2.setText(R.string.drizzle)
            "Tornado" -> tv2.setText(R.string.tornado)
            "Snow" -> tv2.setText(R.string.snow)
            "Rain" -> tv2.setText(R.string.rain)
        }
    }

}