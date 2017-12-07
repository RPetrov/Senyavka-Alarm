package ui

import android.content.Context
import android.preference.Preference
import android.util.AttributeSet
import rpetrov.senyavkaspeakingalarmclock.providers.text.MusicProvider

/**
 * Created by Roman Petrov
 */
class TryMusicPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {
    override fun onClick() {
        MusicProvider(context).run()
    }

    override fun setSummary(summary: CharSequence?) {
        super.setSummary("")
    }
}