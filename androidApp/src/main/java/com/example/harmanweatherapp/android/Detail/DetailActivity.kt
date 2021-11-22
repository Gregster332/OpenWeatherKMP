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

        var tv: TextView = findViewById(R.id.textName)
        var button = findViewById<Button>(R.id.back)
        val b = intent.getStringExtra("welcome")
        tv.text = b

        button.setOnClickListener {
            finish()
        }

    }


}