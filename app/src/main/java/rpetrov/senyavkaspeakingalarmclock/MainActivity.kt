package rpetrov.senyavkaspeakingalarmclock

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.TimePicker
import butterknife.bindView
import rpetrov.senyavkaspeakingalarmclock.providers.CurrentTimeProvider
import rpetrov.senyavkaspeakingalarmclock.providers.NowDateProvider
import rpetrov.senyavkaspeakingalarmclock.providers.ProviderBuildTask
import rpetrov.senyavkaspeakingalarmclock.providers.weather.WeatherProvider
import java.util.*


class MainActivity : AppCompatActivity() {


    val toolbar: Toolbar by bindView(R.id.toolbar)
    val time: TextView by bindView(R.id.time)

    val state: State? = null

    override fun onStart() {
        super.onStart()// restore state
    }

    override fun onStop() {
        super.onStop() // save state
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)

        setSupportActionBar(toolbar)

        checkPermissions()

        val texts = ProviderBuildTask().execute(CurrentTimeProvider(), NowDateProvider(), WeatherProvider(this))

    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    0)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun onTimeClick(view: View): Unit {
        val calendar: Calendar = Calendar.getInstance()
        val tpd = TimePickerDialog(this, this::onTimeSet, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        tpd.show()
    }

    private fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        time.text = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)


    }

    fun setUpAlarm(hourOfDay: Int, minute: Int): Unit {
        val calendar: Calendar = Calendar.getInstance()
        val now: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        if (calendar.before(now)) {
            calendar.timeInMillis += 24 * 60 * 60 * 1000 // add one day
        }

        val i = Intent(this, AlarmBroadcastReceiver::class.java)
        val pi = PendingIntent.getBroadcast(this, 0, i, 0)


        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
    }


}
