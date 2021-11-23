package com.example.harmanweatherapp.android.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.harmanweatherapp.Models.Welcome
import com.example.harmanweatherapp.ViewModels.SimpleViewModel
import com.example.harmanweatherapp.android.Detail.DetailActivity
import com.example.harmanweatherapp.android.R

class MainActivity : AppCompatActivity() {

    //var list: ListView? = null
    var items: ArrayList<Welcome> = arrayListOf()
    var adapter: ListAdapter? = null

    private val viewModel = SimpleViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var list: ListView = findViewById(R.id.listview)
        var editText: EditText = findViewById(R.id.searchview)
        var imageView: ImageView = findViewById(R.id.add)

        //items = viewModel.fetchAllCities()
        adapter = ListAdapter(this, items)
        list.adapter = adapter

        list.setOnItemClickListener { parent, view, position, id ->
            val item = items.get(position)
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("name", item.name)
            intent.putExtra("desc", item.weather[0].main)
            intent.putExtra("temp", item.main.temp.toString())
            intent.putExtra("hum", item.main.humidity.toString())
            intent.putExtra("pres", item.main.pressure.toString())
            intent.putExtra("sunset", item.sys.sunset.toString())
            intent.putExtra("sunrise", item.sys.sunrise.toString())
            intent.putExtra("max", item.main.tempMax.toString())
            intent.putExtra("min", item.main.tempMin.toString())
            intent.putExtra("fl", item.main.feelsLike.toString())
            startActivity(intent)
        }

        imageView.setOnClickListener {
            var currentName = ""
            viewModel.addCityToDB(editText.text.toString())
            print(viewModel.counter.value)
            if (viewModel.counter.value.name != "NONE" &&
                viewModel.counter.value.name != currentName &&
                    !editText.text.isEmpty()) {
                items.add(viewModel.counter.value)
                currentName = viewModel.counter.value.name
                list.adapter = adapter
                editText.setText("")
            } else {
                print("Error")
            }
        }
    }
}
