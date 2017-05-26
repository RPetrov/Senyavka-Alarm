package rpetrov.senyavkaspeakingalarmclock.providers

import com.google.gson.Gson
import com.ibm.icu.text.RuleBasedNumberFormat
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.*

/**
 * Created by Roman Petrov
 */
class CurrencyProvider : IProvider {

    data class Rates(val RUB: String)
    data class Result(val rates: Rates)

    var result: Result? = null

    override fun prepare(): Boolean {

        val gson: Gson = Gson()
        result = gson.fromJson(BufferedReader(InputStreamReader(URL("http://api.fixer.io/latest?base=USD").openStream())), Result::class.java)

        return result != null
    }

    override fun getText(): String {
        val res = result!!

        val r = "%2d".format(res.rates.RUB.toDouble().toInt())
        val k = "%2d".format(((res.rates.RUB.toDouble() - res.rates.RUB.toDouble().toInt())*100).toInt())

        var ruleBasedNumberFormat = RuleBasedNumberFormat(Locale("ru"), RuleBasedNumberFormat.SPELLOUT)

        var text: String = "Курс доллара " + ruleBasedNumberFormat.format(r.toLong()) + " "  + Utils.getCorrectWordForDigit(res.rates.RUB.toDouble().toInt(), " рубль", " рубля", " рублей", " рублей")
        text += ". " + ruleBasedNumberFormat.format(k.toLong()) + " " + Utils.getCorrectWordForDigit((res.rates.RUB.toDouble() - res.rates.RUB.toDouble().toInt()*100).toInt(),
                " копейка", " копейки", " копеек", " копеек")

        return text
    }

}