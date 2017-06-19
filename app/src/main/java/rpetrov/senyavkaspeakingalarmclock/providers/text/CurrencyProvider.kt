package rpetrov.senyavkaspeakingalarmclock.providers.text

import com.ibm.icu.text.RuleBasedNumberFormat

/**
 * Created by Roman Petrov
 */
class CurrencyProvider : rpetrov.senyavkaspeakingalarmclock.providers.ITextProvider {

    data class Rates(val RUB: String)
    data class Result(val rates: rpetrov.senyavkaspeakingalarmclock.providers.text.CurrencyProvider.Rates)

    var result: rpetrov.senyavkaspeakingalarmclock.providers.text.CurrencyProvider.Result? = null

    override fun prepare(): Boolean {

        val gson: com.google.gson.Gson = com.google.gson.Gson()
        result = gson.fromJson(java.io.BufferedReader(java.io.InputStreamReader(java.net.URL("http://api.fixer.io/latest?base=USD").openStream())), rpetrov.senyavkaspeakingalarmclock.providers.text.CurrencyProvider.Result::class.java)

        return result != null
    }

    override fun getText(): String {
        val res = result!!

        val r = "%2d".format(res.rates.RUB.toDouble().toInt())
        val k = "%2d".format(((res.rates.RUB.toDouble() - res.rates.RUB.toDouble().toInt())*100).toInt())

        var ruleBasedNumberFormat = com.ibm.icu.text.RuleBasedNumberFormat(java.util.Locale("ru"), RuleBasedNumberFormat.SPELLOUT)

        var text: String = "Курс доллара " + ruleBasedNumberFormat.format(r.toLong()) + " "  + rpetrov.senyavkaspeakingalarmclock.providers.Utils.getCorrectWordForDigit(res.rates.RUB.toDouble().toInt(), " рубль", " рубля", " рублей", " рублей")
        text += ". " + ruleBasedNumberFormat.format(k.toLong()) + " " + rpetrov.senyavkaspeakingalarmclock.providers.Utils.getCorrectWordForDigit((res.rates.RUB.toDouble() - res.rates.RUB.toDouble().toInt() * 100).toInt(),
                " копейка", " копейки", " копеек", " копеек")

        return text
    }

    override fun getPermissions(): Array<String> = arrayOf()

    override fun getName() = "Курс доллара"

    override fun getDescription() = ""

    override fun isConfigurable() = false
}