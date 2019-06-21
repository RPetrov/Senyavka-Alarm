package rpetrov.senyavkaspeakingalarmclock

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.os.Build
import android.os.PowerManager
import android.preference.PreferenceManager
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import rpetrov.senyavkaspeakingalarmclock.providers.ProviderBuildTask
import rpetrov.senyavkaspeakingalarmclock.providers.ProvidersFactory
import java.util.*


class AlarmService : Service() {

    val ONGOING_NOTIFICATION_ID = 1

    private var providerBuildTask: ProviderBuildTask? = null
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onBind(intent: Intent) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        intent?.getStringExtra("command")?.let {
            if (it == "stop") {
                clear()
                stopSelf()
                return super.onStartCommand(intent, flags, startId)
            }
        }

        val notificationIntent = Intent(this, AlarmService::class.java)
        notificationIntent.putExtra("command", "stop")
        val pendingIntent = PendingIntent.getService(this, 0, notificationIntent, 0)

        var notificationBuilder =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    Notification.Builder(this, createNotificationChannel("sen_chanel", "Alarm"))
                else
                    Notification.Builder(this)

        val icon = BitmapFactory.decodeResource(resources, R.drawable.alarm)

//        val notification = notificationBuilder
//                .setContentTitle(getText(R.string.app_name))
//                .setContentText("TODO DATE")
//                .setSmallIcon(R.drawable.alarm)
//                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
//                .setContentIntent(pendingIntent)
//                .setTicker(getText(R.string.app_name))
//                //  .setStyle(Notification.BigTextStyle().bigText("Much longer text that cannot fit one line..."))
//                .setPriority(Notification.PRIORITY_MAX)
//                .addAction(-1, "Выключить",
//                        pendingIntent)
//                .build()

        val notification = notificationBuilder
                .setContentTitle(getText(R.string.app_name))
                .setContentText(Date().toString())
                .setSmallIcon(R.drawable.alarm)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.app_name))
                .addAction(Notification.Action.Builder(android.R.drawable.ic_notification_clear_all, "Выключить", pendingIntent).build())
                .build()

        startForeground(ONGOING_NOTIFICATION_ID, notification)

        doSpeak()

        return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH)
        chan.lightColor = ContextCompat.getColor(this, R.color.colorPrimary)
        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun clear() {
        providerBuildTask?.cancel()
        wakeLock?.let {
            if (it.isHeld)
                wakeLock?.release()
        }
    }

    private fun doSpeak() {
        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val vol = am.getStreamVolume(AudioManager.STREAM_ALARM)
        am.setStreamVolume(AudioManager.STREAM_ALARM, vol, 0)

        val o = getSystemService(Context.POWER_SERVICE)
        if (o is PowerManager) {
            wakeLock = o.newWakeLock(PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, "sen_alarm:lock_alarm")

            wakeLock?.acquire(1000 * 60 * 3) // 3 minutes
        }

        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sp.edit().putBoolean("enableAlarm.isChecked", false).apply()

        providerBuildTask = ProviderBuildTask(this, ProvidersFactory.getAllTextProvider(this), ProvidersFactory.getAllRunnableProvider(this))
        providerBuildTask?.execute()
    }


    override fun onUnbind(intent: Intent?): Boolean {
        providerBuildTask?.cancel()
        wakeLock?.let {
            if (it.isHeld)
                wakeLock?.release()
        }
        return super.onUnbind(intent)
    }


}
