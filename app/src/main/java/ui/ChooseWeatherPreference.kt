package ui

import android.content.Context
import android.content.SharedPreferences
import android.preference.Preference
import android.preference.PreferenceManager
import android.util.AttributeSet
import org.jetbrains.anko.intentFor
import rpetrov.senyavkaspeakingalarmclock.MapActivity
import rpetrov.senyavkaspeakingalarmclock.providers.text.MusicProvider

/**
 * Created by Roman Petrov
 */
class ChooseWeatherPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {
    override fun onClick() {
        context.startActivity(context.intentFor<MapActivity>())
    }

    override fun setSummary(summary: CharSequence?) {
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val lat = sp.getFloat("whether_lat", 0f)
        val lon = sp.getFloat("whether_lon", 0f)

        val summary = if(lat == 0f || lon == 0f) {
            ""
        } else{
            "$lat, $lon"
        }
        super.setSummary(summary)
    }
}