package rpetrov.senyavkaspeakingalarmclock.providers

import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

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

        var text: String = "Курс доллара " + r + " "  + Utils.getCorrectWordForDigit(res.rates.RUB.toDouble().toInt(), " рубль", " рубля", " рублей", " рублей")
        text += ". " + (k) + Utils.getCorrectWordForDigit((res.rates.RUB.toDouble() - res.rates.RUB.toDouble().toInt()*100).toInt(),
                " копейка", " копейки", " копеек", " копейки")

        return text
    }

}