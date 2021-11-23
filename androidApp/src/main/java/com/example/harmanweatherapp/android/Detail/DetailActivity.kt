package com.example.harmanweatherapp.android.Detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.harmanweatherapp.Models.Welcome
import com.example.harmanweatherapp.android.R

class DetailActivity : AppCompatActivity() {

    var welcome: String? = null

    //var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var tv1: TextView = findViewById(R.id.cityName)
        var tv2: TextView = findViewById(R.id.description)
        var tv3: TextView = findViewById(R.id.temperatureText)
        var pressure: TextView = findViewById(R.id.pressure)
        var humidity: TextView = findViewById(R.id.humidity)
        var sunset: TextView = findViewById(R.id.sunset)
        var sunrise: TextView = findViewById(R.id.sunrise)
        var max: TextView = findViewById(R.id.max)
        var min: TextView = findViewById(R.id.min)
        var feelsLike: TextView = findViewById(R.id.fl)
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
        tv1.text = name
        tv2.text = desc
        tv3.text = temp
        humidity.text = "Humidity: $humidityStr %"
        pressure.text = "Pressure: $pressureStr hPh"
        sunset.text = "Sunset: $sunsetStr"
        sunrise.text = "Sunrise: $sunriseStr"
        max.text = "Temp max: $maxStr ºC"
        min.text = "Temp min: $minStr ºC"
        feelsLike.text = "Feels like: $fl ºC"

        button.setOnClickListener {
            finish()
        }

    }


}