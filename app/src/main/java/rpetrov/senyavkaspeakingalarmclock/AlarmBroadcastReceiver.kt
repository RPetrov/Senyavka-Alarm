package rpetrov.senyavkaspeakingalarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.view.WindowManager
import org.jetbrains.anko.intentFor


/**
 * Created by Roman Petrov
 */

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if(BuildConfig.DEBUG)
            Log.i("AlarmBroadcastReceiver", "AlarmBroadcastReceiver")

         if(BuildConfig.DEBUG)
            Log.i("AlarmBroadcastReceiver", "Starting activity...")

        context.startService(context.intentFor<AlarmService>())


    }
}
