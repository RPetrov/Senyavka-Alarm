package rpetrov.senyavkaspeakingalarmclock.providers

import java.util.*

/**
 * Created by Roman Petrov
 */
class CurrentTimeProvider : IProvider {
    override fun prepare(): Boolean {
        return true
    }

    override fun getText(): String {
        val now: Calendar = Calendar.getInstance()

        val hours = now.get(Calendar.HOUR_OF_DAY)
        val minutes = now.get(Calendar.MINUTE)



        val stringResult: String =  if(hours in 6..12 && now.get(Calendar.MINUTE) == 0){
            "Сейчас уже " + now.get(Calendar.HOUR_OF_DAY) + " "+ getHour(now.get(Calendar.HOUR_OF_DAY)) + " утра."
        } else{
            "Сейчас уже " + now.get(Calendar.HOUR_OF_DAY) + " "+ getHour(now.get(Calendar.HOUR_OF_DAY)) + ", " +
                    + now.get(Calendar.MINUTE) + getMinutes(now.get(Calendar.MINUTE))
        }

        return stringResult
    }


    private fun getHour(hour: Int): String? {
        return Utils.getCorrectWordForDigit(hour, " час", " часа", " часов", " часов")
    }

    private fun getMinutes(m: Int): String? {
        return Utils.getCorrectWordForDigit(m, " минута", " минуты", " минут", " минут")
    }

    override fun getPermissions(): Array<String>  = arrayOf()

    override fun getName() = "Время"

    override fun getDescription() = ""

    override fun isConfigurable() = false
}