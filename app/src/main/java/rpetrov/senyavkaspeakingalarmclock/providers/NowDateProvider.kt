package rpetrov.senyavkaspeakingalarmclock.providers

import java.util.*

/**
 * Created by Roman Petrov
 */

class NowDateProvider : IProvider {
    override fun prepare(): Boolean {
        return true
    }

    override fun getText(): String {
        val now: Calendar = Calendar.getInstance()


        val stringResult: String = getDay(now.get(Calendar.DAY_OF_MONTH)) + " " + getMonth(now.get(Calendar.MONTH) + 1) + ". "+ getDayOfWeek(now.get(Calendar.DAY_OF_WEEK))
        return stringResult
    }


    private fun getDay(day: Int): String? {
        return when (day){
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
            21 -> "двадцать первое"
            22 -> "двадцать второе"
            23 -> "двадцать третье"
            24 -> "двадцать четвертое"
            25 -> "двадцать пятое"
            26 -> "двадцать шестое"
            27 -> "двадцать седьмое"
            28 -> "двадцать восьмое"
            29 -> "двадцать девятое"
            30 -> "тридцать первое"
            31 -> "тридцать второе"
            20 -> "двадцатое"
            else -> ""

        }
    }

    private fun getMonth(day: Int): String? {
        return when (day){
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
        return when (day){
            Calendar.MONDAY -> "понедельник"
            Calendar.TUESDAY -> "вторник"
            Calendar.WEDNESDAY -> "среда"
            Calendar.THURSDAY -> "четверг"
            Calendar.FRIDAY -> "пятница"
            Calendar.SATURDAY -> "суббота"
            Calendar.SUNDAY -> "воскресенье"
            else -> ""
        }

    }



}