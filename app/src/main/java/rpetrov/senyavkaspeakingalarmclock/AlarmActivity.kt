package rpetrov.senyavkaspeakingalarmclock

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import rpetrov.senyavkaspeakingalarmclock.providers.*
import rpetrov.senyavkaspeakingalarmclock.providers.weather.WeatherProvider


class AlarmActivity : AppCompatActivity() {


    private var providerBuildTask: ProviderBuildTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sp.edit().putBoolean("enableAlarm.isChecked", false).apply()

        providerBuildTask = ProviderBuildTask(this)
        providerBuildTask?.execute(CurrentTimeProvider(), NowDateProvider(), WeatherProvider(this), CurrencyProvider(), CalendarProvider(this))
    }

    fun onCancelClick(view: View): Unit {
        finish()
    }


    override fun onStop() {
        super.onStop()
        providerBuildTask?.cancel()
    }
}
