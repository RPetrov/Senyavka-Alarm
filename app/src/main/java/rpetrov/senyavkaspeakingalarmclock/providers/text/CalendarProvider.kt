package rpetrov.senyavkaspeakingalarmclock.providers.text

import android.content.Context
import com.ibm.icu.text.RuleBasedNumberFormat


/**
 * Created by Roman Petrov
 */
class CalendarProvider : BaseProvider, rpetrov.senyavkaspeakingalarmclock.providers.ITextProvider {

    var count: Int = 0

    constructor(context: Context) : super(context)


    override fun prepare(): Boolean {

        val calendar: java.util.Calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)

        val eventsUriBuilder = android.provider.CalendarContract.Instances.CONTENT_URI
                .buildUpon()
        android.content.ContentUris.appendId(eventsUriBuilder, calendar.timeInMillis)
        android.content.ContentUris.appendId(eventsUriBuilder, calendar.timeInMillis + 1000*60*60*23)
        val eventsUri = eventsUriBuilder.build()
        var cursor: android.database.Cursor? = null

        try {
            cursor = context.contentResolver.query(eventsUri, arrayOf("title"), null, null, android.provider.CalendarContract.Instances.DTSTART + " ASC")
        } catch(e: Exception) {
            return false
        }

        count = cursor.count

        cursor.close()

        return count > 0
    }

    override fun getText(): String {

        val ruleBasedNumberFormat = com.ibm.icu.text.RuleBasedNumberFormat(java.util.Locale("ru"), RuleBasedNumberFormat.SPELLOUT)


        return "На сегодня заплонировано " + count + rpetrov.senyavkaspeakingalarmclock.providers.Utils.getCorrectWordForDigit(count, " событие", " события", " событий", " событий")
    }

    override fun getPermissions(): Array<String> = arrayOf(android.Manifest.permission.READ_CALENDAR)

    override fun getName() = "События на сегодня"

    override fun getDescription() = ""

    override fun isConfigurable() = false
}