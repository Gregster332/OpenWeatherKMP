package com.example.harmanweatherapp.android.Settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.example.harmanweatherapp.android.R
import com.example.harmanweatherapp.android.UserSettings

class SettingsActivity: AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var switch: Switch
    var hide = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        backButton = findViewById(R.id.backback)
        switch = findViewById(R.id.switchSett)
        switch.isChecked = loadData()

        var intent = Intent()

        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                hide = true
                saveData(true)
            } else {
                hide = false
                saveData(false)
            }
        }

        backButton.setOnClickListener {
//            intent.putExtra("isHide", hide)
//            setResult(1, intent)
            finish()
        }
    }

    private fun saveData(value: Boolean) {
        val prefs = getSharedPreferences("sp", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.apply {
            putBoolean("HIDE_KEY", value)
        }.apply()
    }

    private fun loadData(): Boolean {
        val prefs = getSharedPreferences("sp", Context.MODE_PRIVATE)
        val savedData = prefs.getBoolean("HIDE_KEY", false)
        return savedData
    }
}