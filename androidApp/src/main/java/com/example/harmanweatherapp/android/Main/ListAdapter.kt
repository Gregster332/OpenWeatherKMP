package com.example.harmanweatherapp.android.Main

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.harmanweatherapp.Models.Welcome
import com.example.harmanweatherapp.android.Constants.emojis
import com.example.harmanweatherapp.android.R

class ListAdapter(private val context: Activity, val arrayList: ArrayList<Welcome>): ArrayAdapter<Welcome>(context, R.layout.list_item, arrayList) {

    lateinit var nameText: TextView
    lateinit var descText: TextView
    lateinit var tempText: TextView
    lateinit var image: TextView

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_item, null)

        nameText = view.findViewById(R.id.nameCity)
        descText = view.findViewById(R.id.descText)
        tempText = view.findViewById(R.id.tempText)
        image = view.findViewById(R.id.imageWeather)

        nameText.text = arrayList[position].name
        //descText.text = arrayList[position].weather[0].main
        setCurrentLanguageLabel(arrayList[position].weather[0].main)
        tempText.text = "${(arrayList[position].main.temp - 273).toInt()}ºC"
        image.text = emojis[arrayList[position].weather[0].main]

        return view
    }

    private fun setCurrentLanguageLabel(text: String) {
        when(text) {
            "Clear" -> descText.setText(R.string.clear)
            "Clouds" -> descText.setText(R.string.clouds)
            "Mist" -> descText.setText(R.string.mist)
            "Smoke" -> descText.setText(R.string.smoke)
            "Haze" -> descText.setText(R.string.haze)
            "Dust" -> descText.setText(R.string.dust)
            "Thunderstorm" -> descText.setText(R.string.thunderstorm)
            "Fog" -> descText.setText(R.string.fog)
            "Sand" -> descText.setText(R.string.sand)
            "Ash" -> descText.setText(R.string.ash)
            "Squall" -> descText.setText(R.string.squall)
            "Drizzle" -> descText.setText(R.string.drizzle)
            "Tornado" -> descText.setText(R.string.tornado)
            "Snow" -> descText.setText(R.string.snow)
            "Rain" -> descText.setText(R.string.rain)
        }
    }

}