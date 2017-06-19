package rpetrov.senyavkaspeakingalarmclock

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PowerManager
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import butterknife.bindView
import rpetrov.senyavkaspeakingalarmclock.providers.text.weather.WeatherProvider
import java.text.SimpleDateFormat
import java.util.*
import android.view.WindowManager
import rpetrov.senyavkaspeakingalarmclock.providers.ProvidersFactory
import rpetrov.senyavkaspeakingalarmclock.providers.text.*


class AlarmActivity : AppCompatActivity() {


    val time: TextView by bindView(R.id.time)

    private var providerBuildTask: ProviderBuildTask? = null
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val o = getSystemService(Context.POWER_SERVICE);
        if(o is PowerManager){
            wakeLock = o.newWakeLock(PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, "SEN_ALARM")

            wakeLock?.acquire()
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                +WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                +WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)


        setContentView(R.layout.activity_alarm)

        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sp.edit().putBoolean("enableAlarm.isChecked", false).apply()

        providerBuildTask = ProviderBuildTask(this, ProvidersFactory.getAllTextProvider(this), ProvidersFactory.getAllRunnableProvider(this))
        providerBuildTask?.execute()

        time.text = SimpleDateFormat("HH:mm").format(Date())
    }

    fun onCancelClick(view: View): Unit {
        providerBuildTask?.cancel()
        finish()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        wakeLock?.release()
    }
}
