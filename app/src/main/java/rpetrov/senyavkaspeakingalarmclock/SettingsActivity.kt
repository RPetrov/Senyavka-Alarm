package rpetrov.senyavkaspeakingalarmclock

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceManager
import org.jetbrains.anko.intentFor


/**
 * Created by Roman Petrov
 */
class SettingsActivity : PreferenceActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(intent.getIntExtra("LAYOUT", -1))
    }


    private fun updateSummariesForDatabaseConfig() {
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        for (i in 0..preferenceScreen.preferenceCount - 1) {
            val preference = preferenceScreen.getPreference(i)
            preference.summary = (sp.all.get(preference.key)).toString()
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        updateSummariesForDatabaseConfig()
    }

    override fun onResume() {
        super.onResume()
        updateSummariesForDatabaseConfig()
    }

    override fun onStart() {
        super.onStart()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}