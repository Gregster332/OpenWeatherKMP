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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_item, null)

        val nameText: TextView = view.findViewById(R.id.nameCity)
        val descText: TextView = view.findViewById(R.id.descText)
        val tempText: TextView = view.findViewById(R.id.tempText)
        val image: TextView = view.findViewById(R.id.imageWeather)

        nameText.text = arrayList[position].name
        descText.text = arrayList[position].weather[0].main
        tempText.text = "Temperature: ${(arrayList[position].main.temp - 273).toInt()}ºC"
        image.text = emojis[arrayList[position].weather[0].main]

        return view
    }

}