package rpetrov.senyavkaspeakingalarmclock.providers.text

import android.content.Context
import android.provider.CalendarContract.Instances.DTSTART
import android.util.Log
import rpetrov.senyavkaspeakingalarmclock.providers.Utils


/**
 * Created by Roman Petrov
 */
class CalendarProvider : BaseProvider, rpetrov.senyavkaspeakingalarmclock.providers.ITextProvider {

    var events: MutableList<String> = arrayListOf()

    constructor(context: Context) : super(context)


    override fun prepare(): Boolean {

        val calendar: java.util.Calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)

        val eventsUriBuilder = android.provider.CalendarContract.Instances.CONTENT_URI
                .buildUpon()
        android.content.ContentUris.appendId(eventsUriBuilder, calendar.timeInMillis)
        android.content.ContentUris.appendId(eventsUriBuilder, calendar.timeInMillis + 1000 * 60 * 60 * 23)
        val eventsUri = eventsUriBuilder.build()

        events = arrayListOf()

        try {
            context.contentResolver.query(
                    eventsUri,
                    arrayOf("title"),
                    null,
                    null,
                    "$DTSTART ASC")
                    .use {
                        it?.let {
                            while (it.moveToNext()) {
                                events.add(it.getString(0))
                            }
                        }
                    }

        } catch (e: Exception) {
            Log.w("CalendarProvider", e.message, e)
            return false
        }

        return events.size > 0
    }

    override fun getText(): String {

        // val ruleBasedNumberFormat = com.ibm.icu.text.RuleBasedNumberFormat(java.util.Locale("ru"), RuleBasedNumberFormat.SPELLOUT)

        val preview = "На сегодня заплонировано " + events.size + Utils.getCorrectWordForDigit(events.size, " событие", " события", " событий", " событий")
        val eventsString = events.joinToString { s -> s }

        return "$preview\\. $eventsString"
    }

    override fun getConfigLayout(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPermissions(): Array<String> = arrayOf(android.Manifest.permission.READ_CALENDAR)

    override fun getName() = "События на сегодня"

    override fun getDescription() = ""

    override fun isConfigurable() = false
}