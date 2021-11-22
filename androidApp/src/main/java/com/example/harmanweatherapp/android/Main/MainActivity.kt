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
    val items: ArrayList<Welcome> = arrayListOf()
    var adapter: ListAdapter? = null

    private val viewModel = SimpleViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var list: ListView = findViewById(R.id.listview)
        var editText: EditText = findViewById(R.id.searchview)
        var imageView: ImageView = findViewById(R.id.add)


        adapter = ListAdapter(this, items)
        list.adapter = adapter

        list.setOnItemClickListener { parent, view, position, id ->
            val item = items.get(position)
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("welcome", item.name)
            startActivity(intent)
        }

        imageView.setOnClickListener {
            var currentName = ""
            viewModel.onCounterButtonPressed(editText.text.toString())
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