package com.example.harmanweatherapp.android

import android.app.Application

class UserSettings: Application() {

    companion object {
        const val USER_SETTINGS = "USER_SETTINGS"
        const val HIDE_TRUE = "HIDE_TRUE"
        const val HIDE_FALSE = "HIDE_FALSE"
    }

    private var hide = false



}