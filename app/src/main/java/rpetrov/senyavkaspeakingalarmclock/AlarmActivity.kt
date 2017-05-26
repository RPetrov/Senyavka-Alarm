package rpetrov.senyavkaspeakingalarmclock

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import rpetrov.senyavkaspeakingalarmclock.providers.*
import rpetrov.senyavkaspeakingalarmclock.providers.weather.WeatherProvider


class AlarmActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sp.edit().putBoolean("enableAlarm.isChecked", false).apply()

        ProviderBuildTask(this).execute(CurrentTimeProvider(), NowDateProvider(), WeatherProvider(this), CurrencyProvider(), CalendarProvider(this))
    }



}
