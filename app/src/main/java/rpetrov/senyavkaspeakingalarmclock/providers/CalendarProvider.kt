package rpetrov.senyavkaspeakingalarmclock.providers

import android.provider.CalendarContract
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import java.util.*


/**
 * Created by Roman Petrov
 */
class CalendarProvider : IProvider{

    val context: Context
    var count: Int = 0

    constructor(context: Context) {
        this.context = context
    }


    override fun prepare(): Boolean {

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val eventsUriBuilder = CalendarContract.Instances.CONTENT_URI
                .buildUpon()
        ContentUris.appendId(eventsUriBuilder, calendar.timeInMillis)
        ContentUris.appendId(eventsUriBuilder, calendar.timeInMillis + 1000*60*60*23)
        val eventsUri = eventsUriBuilder.build()
        var cursor: Cursor? = null

        try {
            cursor = context.contentResolver.query(eventsUri, arrayOf("title"), null, null, CalendarContract.Instances.DTSTART + " ASC")
        } catch(e: Exception) {
            return false
        }

        count = cursor.count

        cursor.close()

        return count > 0
    }

    override fun getText(): String {
        return "На сегодня заплонировано " + count + Utils.getCorrectWordForDigit(count, " событие", " события", " событий", " событий")
    }
}