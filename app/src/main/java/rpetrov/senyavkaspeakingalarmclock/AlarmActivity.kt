package rpetrov.senyavkaspeakingalarmclock

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import rpetrov.senyavkaspeakingalarmclock.providers.*
import rpetrov.senyavkaspeakingalarmclock.providers.weather.WeatherProvider

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)



        ProviderBuildTask().execute(CurrentTimeProvider(), NowDateProvider(), WeatherProvider(this), CurrencyProvider(), CalendarProvider(this))
    }

}
