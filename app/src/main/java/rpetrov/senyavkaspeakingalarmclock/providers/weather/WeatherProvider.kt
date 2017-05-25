package rpetrov.senyavkaspeakingalarmclock.providers.weather

import android.content.Context
import android.location.Location
import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import rpetrov.senyavkaspeakingalarmclock.providers.IProvider
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by Roman Petrov
 */


const val URL: String = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&APPID=74e5ddb61377cec9465df223711dddce&lang=ru"

class WeatherProvider : IProvider {


    private val lock = java.lang.Object()
    val context: Context
    var result: Result? = null


    constructor(context: Context) {
        this.context = context
    }


    override fun prepare(): Boolean {

        var mGoogleApiClient: GoogleApiClient? = null

        mGoogleApiClient = GoogleApiClient.Builder(context)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(p0: Bundle?) {
                        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
                        if (mLastLocation != null) {

                            getWeatherByLocation(mLastLocation)

                        } else {

                            val mLocationRequest = LocationRequest()
                            mLocationRequest.interval = 10000
                            mLocationRequest.fastestInterval = 5000
                            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, object : LocationListener {
                                override fun onLocationChanged(p0: Location?) {
                                    getWeatherByLocation(mLastLocation)

                                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
                                }
                            })
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

        synchronized(lock){
            lock.wait()
        }


        return true // todo
    }


    private fun getWeatherByLocation(location: Location)  {

        Thread(Runnable {
            val gson: Gson = Gson()
            result= gson.fromJson(BufferedReader(InputStreamReader(java.net.URL(String.format(URL, location.latitude, location.longitude)).openStream())), Result::class.java)
            lock.notifyAll()
        }).start()

    }

    override fun getText(): String {
        return ""
    }
}