package rpetrov.senyavkaspeakingalarmclock

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.os.PowerManager
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import rpetrov.senyavkaspeakingalarmclock.providers.ProviderBuildTask
import rpetrov.senyavkaspeakingalarmclock.providers.ProvidersFactory


class AlarmService : Service() {

    val ONGOING_NOTIFICATION_ID = 1

    private var providerBuildTask: ProviderBuildTask? = null
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onBind(intent: Intent) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        intent?.getStringExtra("command")?.let {
            if(it == "stop"){
                clear()
                stopSelf()
                return super.onStartCommand(intent, flags, startId)
            }
        }

        val notificationIntent = Intent(this, AlarmService::class.java)
        notificationIntent.putExtra("command", "stop")
        val pendingIntent = PendingIntent.getService(this, 0, notificationIntent, 0)

        val notification = Notification.Builder(this)
                .setContentTitle(getText(R.string.app_name))
            //    .setContentText("TODO DATE") // date
                .setSmallIcon(R.drawable.alarm)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.app_name))
              //  .setStyle(Notification.BigTextStyle().bigText("Much longer text that cannot fit one line..."))
                .setPriority(Notification.PRIORITY_HIGH)
                .addAction(-1, "Выключить",
                        pendingIntent)
                .build()

        startForeground(ONGOING_NOTIFICATION_ID, notification)

        doSpeak()


        return START_NOT_STICKY
    }

    private fun clear(){
        providerBuildTask?.cancel()
        wakeLock?.let {
            if(it.isHeld)
                wakeLock?.release()
        }
    }

    private fun doSpeak(){
        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val vol = am.getStreamVolume(AudioManager.STREAM_ALARM)
        am.setStreamVolume(AudioManager.STREAM_ALARM, vol, 0)


        val o = getSystemService(Context.POWER_SERVICE);
        if(o is PowerManager){
            wakeLock = o.newWakeLock(PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, "SEN_ALARM")

            wakeLock?.acquire(1000 * 60 * 10) // 10 minutes
        }

        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sp.edit().putBoolean("enableAlarm.isChecked", false).apply()

        providerBuildTask = ProviderBuildTask(this, ProvidersFactory.getAllTextProvider(this), ProvidersFactory.getAllRunnableProvider(this))
        providerBuildTask?.execute()
    }


    override fun onUnbind(intent: Intent?): Boolean {
        providerBuildTask?.cancel()
        wakeLock?.let {
            if(it.isHeld)
                wakeLock?.release()
        }
        return super.onUnbind(intent)
    }


}
