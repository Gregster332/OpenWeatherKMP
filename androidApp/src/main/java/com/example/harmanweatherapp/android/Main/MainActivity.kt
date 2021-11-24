package com.example.harmanweatherapp.android.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.harmanweatherapp.Models.*
import com.example.harmanweatherapp.ViewModels.SimpleViewModel
import com.example.harmanweatherapp.android.Detail.DetailActivity
import com.example.harmanweatherapp.android.R

class MainActivity : AppCompatActivity() {

    //var list: ListView? = null
    var items: ArrayList<Welcome> = arrayListOf()
    var adapter: ListAdapter? = null

    private val viewModel = SimpleViewModel()
    private var current = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var list: ListView = findViewById(R.id.listview)
        var editText: EditText = findViewById(R.id.searchview)
        var imageView: ImageView = findViewById(R.id.add)
        var deleteAllImageView: ImageView = findViewById(R.id.deleteAll)

        items = convert()
        adapter = ListAdapter(this, items)
        list.adapter = adapter

        list.setOnItemClickListener { parent, view, position, id ->
            val item = items.get(position)
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("name", item.name)
            intent.putExtra("desc", item.weather[0].main)
            intent.putExtra("temp", (item.main.temp - 273).toInt().toString())
            intent.putExtra("hum", item.main.humidity.toString())
            intent.putExtra("pres", item.main.pressure.toString())
            intent.putExtra("sunset", item.sys.sunset.toString())
            intent.putExtra("sunrise", item.sys.sunrise.toString())
            intent.putExtra("max", (item.main.tempMax - 273).toInt().toString())
            intent.putExtra("min", (item.main.tempMin - 273).toInt().toString())
            intent.putExtra("fl", (item.main.feelsLike - 273).toInt().toString())
            startActivity(intent)
        }

        list.setOnItemLongClickListener { parent, view, position, id ->
            val item = items.get(position)
            val t = Toast.makeText(applicationContext, "${item.name} deleted", Toast.LENGTH_SHORT)
            t.show()
            viewModel.deleteCity(item.name)
            //viewModel.addCityToDB(item.name)
            items.removeAt(position)
            //print(items.get(0))
            adapter!!.notifyDataSetChanged()
            list.adapter = adapter
            true
        }

        imageView.setOnClickListener {
            //print(viewModel.counter.value.name)
            if (viewModel.counter.value.name != current || viewModel.counter.value.name != "NONE") {
                viewModel.addCityToDB(editText.text.toString())
                if (viewModel.counter.value.name != "NONE" &&
                    viewModel.counter.value.name != current &&
                    !editText.text.isEmpty()
                ) {

                    items.add(viewModel.counter.value)
//                if (viewModel.counter.value.name == viewModel.fetchAllCities().sortedBy { it.name }.last().name) {
//                   viewModel.deleteCity(viewModel.fetchAllCities().last().name)
//                }
                    current = viewModel.counter.value.name
                    list.adapter = adapter
                    editText.setText("")
                } else {
                    print("Error")
                }
            } else {
                print("dhfdh")
            }
        }

        deleteAllImageView.setOnClickListener {
            viewModel.deleteAllCities()
            items.clear()
            adapter!!.notifyDataSetChanged()
            list.adapter = adapter
        }
    }

    private fun convert(): ArrayList<Welcome> {
        var arrayList: ArrayList<Welcome> = arrayListOf()
        var list = viewModel.fetchAllCities()
        list.forEach {
            arrayList.add(viewModel.fromRealmCityModelToWelcome(it))
        }
//        if (!arrayList.isEmpty()) {
//            for (city in arrayList.indices - 1) {
//                for (index in arrayList.indices - 1) {
//                    if (arrayList[index].name == arrayList[index + 1].name) {
//                        arrayList.removeAt(index)
//                    }
//                }
//            }
//        }
        return arrayList
    }

}
