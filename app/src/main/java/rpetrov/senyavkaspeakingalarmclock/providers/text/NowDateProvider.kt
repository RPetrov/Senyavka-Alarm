package rpetrov.senyavkaspeakingalarmclock.providers.text

import android.content.Context

/**
 * Created by Roman Petrov
 */

class NowDateProvider : BaseProvider, rpetrov.senyavkaspeakingalarmclock.providers.ITextProvider {
    constructor(context: Context) : super(context)


    override fun prepare(): Boolean {
        return true
    }

    override fun getText(): String {
        val now: java.util.Calendar = java.util.Calendar.getInstance()


        val stringResult: String = getDay(now.get(java.util.Calendar.DAY_OF_MONTH)) + " " + getMonth(now.get(java.util.Calendar.MONTH) + 1) + ". " + getDayOfWeek(now.get(java.util.Calendar.DAY_OF_WEEK))
        return stringResult
    }


    private fun getDay(day: Int): String? {
        return when (day) {
            1 -> "первое"
            2 -> "второе"
            3 -> "третье"
            4 -> "четвертое"
            5 -> "пятое"
            6 -> "шестое"
            7 -> "седьмое"
            8 -> "восьмое"
            9 -> "девятое"
            10 -> "десятое"
            11 -> "одиннадцатое"
            12 -> "двенадцатое"
            13 -> "тринадцатое"
            14 -> "четырнадцатое"
            15 -> "пятнадцатое"
            16 -> "шестнадцатое"
            17 -> "семнадцатое"
            18 -> "восемнадцатое"
            19 -> "девятнадцатое"
            20 -> "двадцатое"
            21 -> "двадцать первое"
            22 -> "двадцать второе"
            23 -> "двадцать третье"
            24 -> "двадцать четвертое"
            25 -> "двадцать пятое"
            26 -> "двадцать шестое"
            27 -> "двадцать седьмое"
            28 -> "двадцать восьмое"
            29 -> "двадцать девятое"
            30 -> "тридцатое"
            31 -> "тридцать первое"
            else -> ""

        }
    }

    private fun getMonth(day: Int): String? {
        return when (day) {
            1 -> "января"
            2 -> "февраля"
            3 -> "марта"
            4 -> "апреля"
            5 -> "мая"
            6 -> "июня"
            7 -> "июля"
            8 -> "августа"
            9 -> "сентября"
            10 -> "октября"
            11 -> "ноября"
            12 -> "декабря"
            else -> ""

        }

    }

    private fun getDayOfWeek(day: Int): String? {
        return when (day) {
            java.util.Calendar.MONDAY -> "понедельник"
            java.util.Calendar.TUESDAY -> "вторник"
            java.util.Calendar.WEDNESDAY -> "среда"
            java.util.Calendar.THURSDAY -> "четверг"
            java.util.Calendar.FRIDAY -> "пятница"
            java.util.Calendar.SATURDAY -> "суббота"
            java.util.Calendar.SUNDAY -> "воскресенье"
            else -> ""
        }

    }

    override fun getConfigLayout(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPermissions(): Array<String> = arrayOf()

    override fun getName() = "Дата"

    override fun getDescription() = ""

    override fun isConfigurable() = false
}