package com.example.harmanweatherapp.android.Main

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
        var dialog = Dialog(this)
        var refreshLayout: SwipeRefreshLayout = findViewById(R.id.swipe)
        var needToRefresh: TextView = findViewById(R.id.needToRefresh)
        dialog.setCanceledOnTouchOutside(true)
        needToRefresh.visibility = View.GONE

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
            items.removeAt(position)
            //adapter!!.notifyDataSetChanged()
            list.adapter = adapter
            false
        }

        imageView.setOnClickListener {

                    viewModel.addCityToDB(editText.text.toString())
                    items.addAll(convert())
                    //current = viewModel.counter.value.name
                    list.adapter = adapter
                    editText.setText("")

//                    dialog.setContentView(R.layout.popup)
//                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                    dialog.show()

                }

        deleteAllImageView.setOnClickListener {
            viewModel.deleteAllCities()
            items.clear()
            adapter!!.notifyDataSetChanged()
            list.adapter = adapter
           // viewModel.getCityByCoordinates()
        }

        refreshLayout.setOnRefreshListener {
            if (items.isEmpty()) {
                items.addAll(convert())
                adapter!!.notifyDataSetChanged()
                needToRefresh.visibility = View.GONE
            } else {
                items.clear()
                viewModel.refresh()
                needToRefresh.visibility = View.VISIBLE
            }
            list.adapter = adapter
            refreshLayout.isRefreshing = false
        }

    }
    private fun convert(): ArrayList<Welcome> {
        var arrayList: ArrayList<Welcome> = arrayListOf()
        viewModel.fetchAllCities()
        var list = viewModel.cities
        list.forEach {
            arrayList.add(viewModel.fromRealmCityModelToWelcome(it))
        }
        return arrayList
    }
}
