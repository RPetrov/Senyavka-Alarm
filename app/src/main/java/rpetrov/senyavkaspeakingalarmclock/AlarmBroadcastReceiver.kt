package rpetrov.senyavkaspeakingalarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.view.WindowManager
import org.jetbrains.anko.intentFor
import android.support.v4.content.ContextCompat.startForegroundService
import android.os.Build




/**
 * Created by Roman Petrov
 */

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if(BuildConfig.DEBUG)
            Log.i("AlarmBroadcastReceiver", "AlarmBroadcastReceiver")

         if(BuildConfig.DEBUG)
            Log.i("AlarmBroadcastReceiver", "Starting activity...")


        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sp.edit().putBoolean("enabled", false).apply()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(context.intentFor<AlarmService>())
        } else {
            context.startService(context.intentFor<AlarmService>())
        }


    }
}
