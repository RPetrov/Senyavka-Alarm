package rpetrov.senyavkaspeakingalarmclock

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import rpetrov.senyavkaspeakingalarmclock.providers.*
import rpetrov.senyavkaspeakingalarmclock.providers.weather.WeatherProvider


class AlarmActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)


        ProviderBuildTask(this).execute(CurrentTimeProvider(), NowDateProvider(), WeatherProvider(this), CurrencyProvider(), CalendarProvider(this))
    }



}
