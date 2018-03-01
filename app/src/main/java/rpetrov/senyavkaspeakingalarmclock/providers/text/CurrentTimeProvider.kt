package rpetrov.senyavkaspeakingalarmclock.providers.text

import android.content.Context

/**
 * Created by Roman Petrov
 */
class CurrentTimeProvider : BaseProvider, rpetrov.senyavkaspeakingalarmclock.providers.ITextProvider {
    constructor(context: Context) : super(context)


    override fun prepare(): Boolean {
        return true
    }


    override fun getText(): String {
        val now: java.util.Calendar = java.util.Calendar.getInstance()

        val hours = now.get(java.util.Calendar.HOUR_OF_DAY)
        val minutes = now.get(java.util.Calendar.MINUTE)


        val stringResult: String = if (hours == 12 && minutes == 0) {
            return "Полдень"
        } else if (hours in 6..12 && minutes == 0) {
            "Сейчас уже ${getTextByNumber(hours)} ${getHour(hours)} утра."
        } else {
            "Сейчас уже ${getTextByNumber(hours)} ${getHour(hours)}, $minutes ${getMinutes(minutes)}"
        }

        return stringResult
    }

    private fun getTextByNumber(number: Int): String {
        return when (number) {
            0 -> "Ноль"
            1 -> "Один"
            2 -> "Два"
            3 -> "Три"
            4 -> "Четыре"
            5 -> "Пять"
            6 -> "Шесть"
            7 -> "Семь"
            8 -> "Восемь"
            9 -> "Девять"
            10 -> "Десять"
            11 -> "Одиннадцать"
            12 -> "двенадцать"
            13 -> "тринадцать"
            14 -> "четырнадцать"
            15 -> "пятнадцать"
            16 -> "шестнадцать"
            17 -> "семнадцать"
            18 -> "восемнадцать"
            19 -> "девятнадцать"
            20 -> "двадцать"
            21 -> "двадцать один"
            22 -> "двадцать два"
            23 -> "двадцать три"
            else -> ""
        }
    }


    private fun getHour(hour: Int): String? {
        return rpetrov.senyavkaspeakingalarmclock.providers.Utils.getCorrectWordForDigit(hour, " час", " часа", " часов", " часов")
    }

    private fun getMinutes(m: Int): String? {
        return rpetrov.senyavkaspeakingalarmclock.providers.Utils.getCorrectWordForDigit(m, " минута", " минуты", " минут", " минут")
    }


    override fun getConfigLayout(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPermissions(): Array<String> = arrayOf()

    override fun getName() = "Время"

    override fun getDescription() = ""

    override fun isConfigurable() = false
}