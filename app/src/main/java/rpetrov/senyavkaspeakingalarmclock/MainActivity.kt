package rpetrov.senyavkaspeakingalarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import rpetrov.senyavkaspeakingalarmclock.kotterknife.bindView
import rpetrov.senyavkaspeakingalarmclock.providers.ProvidersFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {


    val toolbar: Toolbar by bindView(R.id.toolbar)
    val time: TextView by bindView(R.id.time)
    val enableAlarm: SwitchCompat by bindView(R.id.enable_alarm)
    val providersListView: ListView by bindView(R.id.providers)


    var hours: Int = 0
    var minutes: Int = 0
    val EXTRA_ALARM_ID = "rpetrov.senyavkaspeakingalarmclock.EXTRA_ALARM_ID"
    val id = 1


    val alarmManager: AlarmManager
        get() = getSystemService(Context.ALARM_SERVICE) as AlarmManager

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

        if (BuildConfig.DEBUG)
            supportActionBar?.title = "DEBUG" + supportActionBar?.title

        checkPermissions()

        providersListView.adapter = object : BaseAdapter() {

            val providers = ProvidersFactory.getAll(this@MainActivity)

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                var view = convertView
                if (view == null) {
                    view = LayoutInflater.from(this@MainActivity).inflate(R.layout.provider_item, parent, false)
                }

                if (view == null)
                    throw RuntimeException("View is null")

                val name : TextView = view.findViewById(R.id.provider_name)
                val desc : TextView = view.findViewById(R.id.provider_desc)
                val enable : CheckBox = view.findViewById(R.id.provider_enable_box)
                val settings : ImageView= view.findViewById(R.id.provider_settings)
                name.text = providers[position].getName()
                desc.text = providers[position].getDescription()
                enable.isChecked = providers[position].isEnable()
                settings.isEnabled = providers[position].isConfigurable()

                enable.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                        providers[position].enable(isChecked)
                    }
                })

                if (providers[position].isConfigurable()) {
                    view.setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            val i = Intent(this@MainActivity, SettingsActivity::class.java)
                            i.putExtra("LAYOUT", providers[position].getConfigLayout())
                            startActivity(i)
                        }
                    })
                } else {
                    view.setOnClickListener(null)
                }

                // enable

                return view
            }

            override fun getItem(position: Int): Any = providers[position]

            override fun getItemId(position: Int): Long = position.toLong()

            override fun getCount(): Int = providers.size
        }

    }

    override fun onResume() {
        super.onResume()

        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        hours = sp.getInt("hours", 9)
        minutes = sp.getInt("minutes", 0)

        time.text = String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)


        enableAlarm.setOnCheckedChangeListener(null)
        enableAlarm.isChecked = sp.getBoolean("enabled", false)

        enableAlarm.setOnCheckedChangeListener { buttonView, isChecked ->
            if (enableAlarm.isChecked) {
                setUpAlarm()
            } else {
                cancel(alarmManager)
            }
        }
    }

    private fun checkPermissions() {

        val permissions: ArrayList<String> = ArrayList<String>()

        val providers = ProvidersFactory.getAll(this@MainActivity)

        for (p in providers) {

            for (permission in p.getPermissions()) {
                if (ContextCompat.checkSelfPermission(this,
                                permission)
                        != PackageManager.PERMISSION_GRANTED) {

                    permissions.add(permission)
                }
            }
        }

        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissions.toTypedArray(),
                    0)
        }
    }

//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        val id = item.itemId
//
//        if (id == R.id.action_settings) {
//            return true
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

    fun onTimeClick(view: View): Unit {
        val calendar: Calendar = Calendar.getInstance()
        val tpd = TimePickerDialog(this, this::onTimeSet, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        tpd.show()
    }

    private fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        time.text = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
        this.hours = hourOfDay
        this.minutes = minute
        if (enableAlarm.isChecked) {
            setUpAlarm()
        }
    }

    override fun onPause() {
        super.onPause()
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sp.edit().putInt("hours", hours).apply()
        sp.edit().putInt("minutes", minutes).apply()
        sp.edit().putBoolean("enabled", enableAlarm.isChecked).apply()
    }


    private fun setUpAlarm(): Unit {

        val calendar: Calendar = Calendar.getInstance()
        val now: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hours)
        calendar.set(Calendar.MINUTE, minutes)

        if (calendar.before(now)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        setAlarm(alarmManager, calendar.timeInMillis)

        Toast.makeText(this, "Будильник установлен на " + SimpleDateFormat("HH:mm").format(calendar.time), Toast.LENGTH_LONG).show()

    }

    /**
     * The intent to fire when the alarm should ring.
     *
     * @param context       An active context instance.
     * @return              A PendingIntent that will open the alert screen.
     */
    private fun getIntent(context: Context): PendingIntent {
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        intent.putExtra(EXTRA_ALARM_ID, id)
        return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    /**
     * Schedule a time for the alarm to ring at.
     *
     * @param manager       The AlarmManager to schedule the alarm on.
     * @param timeMillis    A UNIX timestamp specifying the next time for the alarm to ring.
     */
    private fun setAlarm(manager: AlarmManager, timeMillis: Long) {
        val time = if (BuildConfig.DEBUG) {
            System.currentTimeMillis() + 5000
        } else {
            timeMillis
        }

        manager.setAlarmClock(
                AlarmManager.AlarmClockInfo(
                        time,
                        PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), 0)
                ),
                getIntent(this)
        )
    }

    /**
     * Cancel the next time for the alarm to ring.
     *
     * @param manager       The AlarmManager that the alarm was scheduled on.
     */
    private fun cancel(manager: AlarmManager) {
        manager.cancel(getIntent(this))
    }


}
