package rpetrov.senyavkaspeakingalarmclock

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import butterknife.bindView
import rpetrov.senyavkaspeakingalarmclock.providers.*
import rpetrov.senyavkaspeakingalarmclock.providers.weather.WeatherProvider
import java.text.SimpleDateFormat
import java.util.*


class AlarmActivity : AppCompatActivity() {


    val time: TextView by bindView(R.id.time)

    private var providerBuildTask: ProviderBuildTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sp.edit().putBoolean("enableAlarm.isChecked", false).apply()

        providerBuildTask = ProviderBuildTask(this)
        providerBuildTask?.execute(CurrentTimeProvider(), NowDateProvider(), WeatherProvider(this), CurrencyProvider(), CalendarProvider(this))

        time.text = SimpleDateFormat("HH:mm").format(Date())
    }

    fun onCancelClick(view: View): Unit {
        finish()
    }


    override fun onStop() {
        super.onStop()
        providerBuildTask?.cancel()
    }
}
