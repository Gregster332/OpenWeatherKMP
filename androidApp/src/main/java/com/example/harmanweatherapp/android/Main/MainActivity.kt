package com.example.harmanweatherapp.android.Main

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.harmanweatherapp.Enums.LoadingState
import com.example.harmanweatherapp.Models.*
import com.example.harmanweatherapp.ViewModels.SimpleViewModel
import com.example.harmanweatherapp.android.Detail.DetailActivity
import com.example.harmanweatherapp.android.R
import com.example.harmanweatherapp.android.Services.NetworkChangeListener
import com.example.harmanweatherapp.android.Services.isOnline
import com.example.harmanweatherapp.android.Settings.SettingsActivity
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    var items: ArrayList<Welcome> = arrayListOf()
    var adapter: ListAdapter? = null

    private val viewModel = SimpleViewModel(EventsDispatcher())
    private var current = ""

    private lateinit var newDescText: TextView
    private lateinit var newDescCityText: TextView
    private lateinit var newTempText: TextView
    private lateinit var goBackFromSettings: Button
    private lateinit var dialog: Dialog
    private lateinit var customButton: RelativeLayout
    private lateinit var cld: NetworkChangeListener
    private lateinit var fusedLocClient: FusedLocationProviderClient

    val rotation: RotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var list: ListView = findViewById(R.id.listview)
        var editText: EditText = findViewById(R.id.searchview)
        var imageView: ImageView = findViewById(R.id.add)
        var deleteAllImageView: ImageView = findViewById(R.id.deleteAll)
        var settingsView: ImageView = findViewById(R.id.settings)

        dialog = Dialog(this)
        var refreshLayout: SwipeRefreshLayout = findViewById(R.id.swipe)
        dialog.setCanceledOnTouchOutside(true)

        customButton = findViewById(R.id.customButton)
        newDescText = findViewById(R.id.descInButton)
        newDescCityText = findViewById(R.id.descInButtonCity)
        newTempText = findViewById(R.id.tempInButton)

        reloadData()
        adapter = ListAdapter(this, items)
        list.adapter = adapter

        fusedLocClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()

        customButton.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("name", viewModel.currentCity.value.name)
            intent.putExtra("desc", viewModel.currentCity.value.weather[0].main)
            intent.putExtra("temp", (viewModel.currentCity.value.main.temp - 273).toInt().toString())
            intent.putExtra("hum", viewModel.currentCity.value.main.humidity.toString())
            intent.putExtra("pres", viewModel.currentCity.value.main.pressure.toString())
            intent.putExtra("sunset", viewModel.currentCity.value.sys.sunset.toString())
            intent.putExtra("sunrise", viewModel.currentCity.value.sys.sunrise.toString())
            intent.putExtra("max", (viewModel.currentCity.value.main.tempMax - 273).toInt().toString())
            intent.putExtra("min", (viewModel.currentCity.value.main.tempMin - 273).toInt().toString())
            intent.putExtra("fl", (viewModel.currentCity.value.main.feelsLike - 273).toInt().toString())
            startActivity(intent)
        }

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
            list.adapter = adapter
            false
        }

        settingsView.setOnClickListener {
           val intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(intent, 101)
        }

        imageView.setOnClickListener {
            if (!isOnline(applicationContext)) {
                dialog.setContentView(R.layout.settings_popup)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            } else {
                dialog.setContentView(R.layout.spinner)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
                viewModel.checkAndAddNewCity(editText.text.toString(), callback = {
                    if (it == LoadingState.success) {
                        dialog.hide()
                        items.clear()
                        reloadData()
                        list.adapter = adapter
                        editText.setText("")
                    } else {
                        dialog.hide()
                        editText.setText("")
                        dialog.setContentView(R.layout.popup)
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.show()
                    }
                })
            }
        }

        deleteAllImageView.setOnClickListener {
            viewModel.realm.deleteAllCities()
            items.clear()
            adapter!!.notifyDataSetChanged()
            list.adapter = adapter
        }

        refreshLayout.setOnRefreshListener {
            if (!isOnline(applicationContext)) {
                dialog.setContentView(R.layout.settings_popup)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
                refreshLayout.isRefreshing = false
            } else {
                viewModel.refresh {
                    viewModel.fetchAllCities()
                    items.clear()
                    reloadData()
                    checkLocationPermission()
                    list.adapter = adapter
                    refreshLayout.isRefreshing = false
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        checkInternetConnection()
        if (!isOnline(applicationContext)) {
            customButton.visibility = View.GONE
        } else {
            checkLocationPermission()
            if (loadData()) {
                customButton.visibility = View.GONE
            } else {
                customButton.visibility = View.VISIBLE
            }
        }
    }

    fun checkInternetConnection() {
       cld = NetworkChangeListener(application)
        cld.observe(this, { isConnected ->
            if (!isConnected) {
                customButton.visibility = View.GONE
            } else {
                if (!loadData()) {
                    customButton.visibility = View.VISIBLE
                }
            }
        })
    }

    fun reloadData() {
        viewModel.fetchAllCities()
        var list = viewModel.cities
        list.forEach {
            items.add(viewModel.fromRealmCityModelToWelcome(it))
        }
    }

    private fun checkLocationPermission() {
        val task = fusedLocClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener { location: Location? ->
            if (location != null) {
                viewModel.getCurrentUserLocation(location.latitude, location.longitude, callback = { result ->
                    if (result != null && result.name != "Globe") {
                        newDescCityText.text = "${result.name}"
                        newDescText.text = "${result.weather[0].main}"
                        newTempText.text = "${(result.main.temp - 273).toInt()}ºC"
                       viewModel.getValueForCurrentCity(result)
                    }
                })
            } else {
                newDescText.setText(R.string.error)
                newTempText.text = ""
            }
        }
    }

    private fun loadData(): Boolean {
        val prefs = getSharedPreferences("sp", Context.MODE_PRIVATE)
        val savedData = prefs.getBoolean("HIDE_KEY", false)
        return savedData
    }
}
