package rpetrov.senyavkaspeakingalarmclock.providers.weather

import android.content.Context
import android.location.Location
import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import rpetrov.senyavkaspeakingalarmclock.providers.IProvider
import rpetrov.senyavkaspeakingalarmclock.providers.Utils
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by Roman Petrov
 */


const val URL: String = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&APPID=74e5ddb61377cec9465df223711dddce&lang=ru&units=metric"

class WeatherProvider : IProvider {


    private val lock = java.lang.Object()
    val context: Context
    var result: Result? = null


    constructor(context: Context) {
        this.context = context
    }


    override fun prepare(): Boolean  {

        var mGoogleApiClient: GoogleApiClient? = null

        mGoogleApiClient = GoogleApiClient.Builder(context)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(p0: Bundle?) {
                        val mLastLocation = try {
                            LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
                        }  catch (e: SecurityException) { null }

                        if (mLastLocation != null) {

                            getWeatherByLocation(mLastLocation)

                        } else {

                            val mLocationRequest = LocationRequest()
                            mLocationRequest.interval = 10000
                            mLocationRequest.fastestInterval = 5000
                            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

                            try {
                                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, object : LocationListener {
                                    override fun onLocationChanged(p0: Location?) {
                                        if(p0 != null)
                                            getWeatherByLocation(p0)
                                        else {
                                            synchronized(lock) {
                                                lock.notifyAll()
                                            }
                                        }

                                        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
                                    }
                                })
                            } catch(e: SecurityException) {
                                synchronized(lock) {
                                    lock.notifyAll()
                                }
                            }
                        }
                    }

                    override fun onConnectionSuspended(p0: Int) {
                        System.out.print(p0)
                    }
                })
                .addOnConnectionFailedListener({ })
                .addApi(LocationServices.API)
                .build()

        mGoogleApiClient.connect()

        synchronized(lock) {
            lock.wait(10000)
        }


        return result != null
    }


    private fun getWeatherByLocation(location: Location) {

        Thread(Runnable {
            val gson: Gson = Gson()
            result = gson.fromJson(BufferedReader(InputStreamReader(java.net.URL(String.format(URL, location.latitude, location.longitude)).openStream())), Result::class.java)

            synchronized(lock) {
                lock.notifyAll()
            }
        }).start()

    }

    override fun getText(): String {

        val res =result ?: return ""

        var text: String = "Сейчас " + res.main.temp.toInt() + " " + Utils.getCorrectWordForDigit(res.main.temp.toInt(), " градус", " градуса", " градусов", " градусов") +  ". "

        text+= " " + res.weather.first().description + ". "

        if(res.wind != null){
            val windVar:Wind = res.wind
            text+= " Скорость ветра " + windVar.speed + Utils.getCorrectWordForDigit(windVar.speed, " метр", " метра", " метров", " метров") + " в секунду."
        }

        return text

    }
}