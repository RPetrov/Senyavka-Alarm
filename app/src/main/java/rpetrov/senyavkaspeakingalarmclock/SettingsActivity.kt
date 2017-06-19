package rpetrov.senyavkaspeakingalarmclock

import android.os.Bundle
import android.preference.PreferenceActivity

/**
 * Created by Roman Petrov
 */
class SettingsActivity : PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(intent.getIntExtra("LAYOUT", -1))
    }

}