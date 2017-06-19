package rpetrov.senyavkaspeakingalarmclock.providers.text

import java.util.*

/**
 * Created by Roman Petrov
 */
class CurrentTimeProvider : rpetrov.senyavkaspeakingalarmclock.providers.ITextProvider {
    override fun prepare(): Boolean {
        return true
    }

    override fun getText(): String {
        val now: java.util.Calendar = java.util.Calendar.getInstance()

        val hours = now.get(java.util.Calendar.HOUR_OF_DAY)
        val minutes = now.get(java.util.Calendar.MINUTE)



        val stringResult: String =  if(hours in 6..12 && now.get(java.util.Calendar.MINUTE) == 0){
            "Сейчас уже " + now.get(java.util.Calendar.HOUR_OF_DAY) + " "+ getHour(now.get(java.util.Calendar.HOUR_OF_DAY)) + " утра."
        } else{
            "Сейчас уже " + now.get(java.util.Calendar.HOUR_OF_DAY) + " "+ getHour(now.get(java.util.Calendar.HOUR_OF_DAY)) + ", " +
                    + now.get(java.util.Calendar.MINUTE) + getMinutes(now.get(java.util.Calendar.MINUTE))
        }

        return stringResult
    }


    private fun getHour(hour: Int): String? {
        return rpetrov.senyavkaspeakingalarmclock.providers.Utils.getCorrectWordForDigit(hour, " час", " часа", " часов", " часов")
    }

    private fun getMinutes(m: Int): String? {
        return rpetrov.senyavkaspeakingalarmclock.providers.Utils.getCorrectWordForDigit(m, " минута", " минуты", " минут", " минут")
    }

    override fun getPermissions(): Array<String>  = arrayOf()

    override fun getName() = "Время"

    override fun getDescription() = ""

    override fun isConfigurable() = false
}