package kz.sfizfaka.android.weatherapitask

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kz.sfizfaka.android.weatherapitask.databinding.ActivityMainBinding
import org.json.JSONObject


private const val API_KEY_1 = "6d65b753875f483dac4115003230702"  //weatherApi.com
private const val API_KEY_2 = "ec20d2dae413a43941ae9983d505c50a" //OpenWeather
private const val API_KEY_3 = ""
class MainActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val longitude = 35.05
        val latitude = 100.45

        fetchLocation()
        binding.server1.setOnClickListener {

         getResult1(latitude,longitude)
            //binding.temperature.text = getResult1(latitude,longitude)

        }
        binding.server2.setOnClickListener {
            getResult2(latitude,longitude)
            //binding.temperature.text = getResult2(latitude,longitude)
        }
//        binding.server3.setOnClickListener {
//            getResult3(latitude,longitude)
//        }


    }

    private fun getResult1(lat: Double?, lon: Double?): String {
        var temperature = ""
        val url = "http://api.weatherapi.com/v1/current.json?" +
                "key=$API_KEY_1&q=$lat,$lon&aqi=no"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val obj = JSONObject(response)
                val temp = obj.getJSONObject("current").getString("temp_c")
                val city = obj.getJSONObject("location").getString("name")
                val date = obj.getJSONObject("current").getString("last_updated")
                temperature = temp
                Log.d("Mylog", "Response: $response")
            },
            {
                Log.d("Mylog", "Error: $it")
            }
        )
        queue.add(stringRequest)
        return temperature
    }

    private fun getResult2(lat: Double?, lon: Double?): String {
        var temperature = ""
        val url = "https://api.openweathermap.org/data/2.5/weather?" +
                "lat=$lat&lon=$lon&appid=$API_KEY_2"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val obj = JSONObject(response)
                val temp = obj.getJSONObject("main").getString("temp")
                val city = obj.getJSONObject("main").getString("name")
                val date = obj.getJSONObject("current").getString("last_updated")
                temperature = temp
                Log.d("Mylog", "Response: $response")
            },
            {
                Log.d("Mylog", "Error: $it")
            }
        )
        queue.add(stringRequest)
        return temperature
    }

    //  private fun getResult3(lat:Double?,lon:Double?){


    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                binding.textView2.text = it.latitude.toString()
                binding.textView3.text = it.latitude.toString()
            }
        }
    }
}