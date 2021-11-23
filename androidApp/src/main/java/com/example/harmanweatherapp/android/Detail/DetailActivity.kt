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
        var button = findViewById<Button>(R.id.back)
        val name = intent.getStringExtra("name")
        val desc = intent.getStringExtra("desc")
        val temp = intent.getStringExtra("temp")
        tv1.text = name
        tv2.text = desc
        tv3.text = temp

        button.setOnClickListener {
            finish()
        }

    }


}