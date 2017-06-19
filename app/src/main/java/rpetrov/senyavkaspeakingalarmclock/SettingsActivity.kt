package rpetrov.senyavkaspeakingalarmclock

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceManager


/**
 * Created by Roman Petrov
 */
class SettingsActivity : PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(intent.getIntExtra("LAYOUT", -1))

        updateSummariesForDatabaseConfig()
    }


    private fun updateSummariesForDatabaseConfig() {
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        for (i in 0..preferenceScreen.preferenceCount - 1) {
            val preference = preferenceScreen.getPreference(i)
            preference.summary = (sp.all.get(preference.key)).toString()

        }
    }
}