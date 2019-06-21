package ui

import android.content.Context
import android.preference.Preference
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
        super.setSummary("")
    }
}