package com.example.weatherping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lat=intent.getStringExtra("lat")
        var long=intent.getStringExtra("long")

        getJsonData(lat,long)
    }

    private fun getJsonData(lat: String?, long: String?) {
        val API_KEY="72623b6d8333b6948614959a56371279"
        val queue = Volley.newRequestQueue(this)
        val url ="https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener { response ->
                setValues(response)
            },
            Response.ErrorListener { Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show() })


        queue.add(jsonRequest)
    }

    private fun setValues(response: JSONObject) {
        city.text = response.getString("name")
        var lat = response.getJSONObject("coord").getString("lat")
        var long=response.getJSONObject("coord").getString("lon")
        weather.text = response.getJSONArray("weather").getJSONObject(0).getString("main")

        var tempr = response.getJSONObject("main").getString("temp")
        tempr=((((tempr).toFloat()-273.15)).toInt()).toString()
        temp.text="${tempr}°C"

        var mintemp = response.getJSONObject("main").getString("temp_min")
        mintemp = ((((mintemp).toFloat()-273.15)).toInt()).toString()
        temp_min.text = "Min Temp: " + mintemp+"°C"

        var maxtemp=response.getJSONObject("main").getString("temp_max")
        maxtemp=((ceil((maxtemp).toFloat() - 273.15)).toInt()).toString()
        temp_max.text = "Max Temp: "+ maxtemp +"°C"

        pressure.text = response.getJSONObject("main").getString("pressure")
        humidity.text = response.getJSONObject("main").getString("humidity")+"%"
        wind.text = response.getJSONObject("wind").getString("speed")

        var updatedAt = response.getLong("dt")
        updated_at.text = "Updated at: "+SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))

        var sunriseTime = response.getJSONObject("sys").getLong("sunrise")
        sunrise.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunriseTime*1000))

        var sunsetTime = response.getJSONObject("sys").getLong("sunset")
        sunset.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunsetTime*1000))
    }


}