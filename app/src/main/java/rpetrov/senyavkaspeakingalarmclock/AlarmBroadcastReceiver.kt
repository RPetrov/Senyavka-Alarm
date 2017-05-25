package rpetrov.senyavkaspeakingalarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


/**
 * Created by Roman Petrov
 */

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val i = Intent(context, AlarmActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)


    }
}
