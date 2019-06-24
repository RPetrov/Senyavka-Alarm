package rpetrov.senyavkaspeakingalarmclock.providers.text.weather

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.ibm.icu.text.RuleBasedNumberFormat
import rpetrov.senyavkaspeakingalarmclock.R
import rpetrov.senyavkaspeakingalarmclock.providers.ITextProvider
import rpetrov.senyavkaspeakingalarmclock.providers.Utils
import rpetrov.senyavkaspeakingalarmclock.providers.text.BaseProvider
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

/**
 * Created by Roman Petrov
 */


const val URL: String = "http://api.openweathermap.org/data/2.5/weather?%s&APPID=74e5ddb61377cec9465df223711dddce&lang=ru&units=metric"
const val PARAM_LOCATION: String = "lat=%s&lon=%s"
const val PARAM_CITY: String = "q=%s"

class WeatherProvider : BaseProvider, ITextProvider {


    // private val lock = java.lang.Object()
    var result: Result? = null
    var location: Location? = null

    constructor(context: Context) : super(context)
    private val lock = java.lang.Object()

    override fun prepare(): Boolean {

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if(ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){

            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        this.location = location
                        synchronized(lock) {lock.notifyAll()}
                    }


            synchronized(lock) {
                lock.wait(90000)
            }
        }

        val location = this.location
        result = if(location != null){
            getWeatherByLocation(location.latitude.toFloat(), location.longitude.toFloat())
        } else{
            val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val lat = sp.getFloat("whether_lat", 0f)
            val lon = sp.getFloat("whether_lon", 0f)

            if(lat == 0f || lon == 0f)
                return false

            getWeatherByLocation(lat, lon)
        }

        return result != null
    }


    private fun getWeatherByLocation(lat : Float, lon : Float): Result {
        val gson: Gson = Gson()
        val param = String.format(PARAM_LOCATION, lat, lon)
        return gson.fromJson(BufferedReader(InputStreamReader(java.net.URL(String.format(URL, param)).openStream())), Result::class.java)
    }

    private fun getWeatherByAddress(city: String): Result {
        val gson: Gson = Gson()
        val param = String.format(PARAM_CITY, city)
        return gson.fromJson(BufferedReader(InputStreamReader(java.net.URL(String.format(URL, param)).openStream())), Result::class.java)
    }

    override fun getText(): String {

        val res = result ?: return ""

        var text: String = "Сейчас " + res.main.temp.toInt() + " " + Utils.getCorrectWordForDigit(res.main.temp.toInt(), " градус", " градуса", " градусов", " градусов") + ". "

        text += " " + res.weather.first().description + ". "

        if (res.wind != null) {
            val windVar: Wind = res.wind

            val ruleBasedNumberFormat = RuleBasedNumberFormat(Locale("ru"), RuleBasedNumberFormat.SPELLOUT)
            text += " Скорость ветра " + ruleBasedNumberFormat.format(windVar.speed.toInt()) + Utils.getCorrectWordForDigit(windVar.speed.toInt(), " метр", " метра", " метров", " метров") + " в секунду."
        }

        return text

    }

    override fun getConfigLayout(): Int = R.xml.weather

    override fun getPermissions(): Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    override fun getName() = "Погода"

    override fun getDescription() = "Сообщает о погоде за окном"

    override fun isConfigurable() = true // todo setup default location!

}