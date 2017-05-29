package rpetrov.senyavkaspeakingalarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.WindowManager


/**
 * Created by Roman Petrov
 */

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val i = Intent(context, AlarmActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        context.startActivity(i)


    }
}
