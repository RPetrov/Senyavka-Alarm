package rpetrov.senyavkaspeakingalarmclock.providers.text

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.ibm.icu.text.RuleBasedNumberFormat


/**
 * Created by Roman Petrov
 */
class CurrencyProvider : BaseProvider, rpetrov.senyavkaspeakingalarmclock.providers.ITextProvider {
    constructor(context: Context) : super(context)

    data class Rates(
            @SerializedName("Date")
            val date: String,
            @SerializedName("PreviousDate")
            val previousDate: String,
            @SerializedName("PreviousURL")
            val previousURL: String,
            @SerializedName("Timestamp")
            val timestamp: String,
            @SerializedName("Valute")
            val valute: Map<String, Rate>
    )

    data class Rate(
            @SerializedName("CharCode")
            val charCode: String,
            @SerializedName("ID")
            val iD: String,
            @SerializedName("Name")
            val name: String,
            @SerializedName("Nominal")
            val nominal: Int,
            @SerializedName("NumCode")
            val numCode: String,
            @SerializedName("Previous")
            val previous: Double,
            @SerializedName("Value")
            val value: Double
    )

    lateinit var preparedText: String

    override fun prepare(): Boolean {

        val gson: com.google.gson.Gson = com.google.gson.Gson()
        val result = gson.fromJson(java.io.BufferedReader(java.io.InputStreamReader(java.net.URL("https://www.cbr-xml-daily.ru/daily_json.js").openStream())), rpetrov.senyavkaspeakingalarmclock.providers.text.CurrencyProvider.Rates::class.java)

        // val r = "%2d".format(result.valute.get("USD")?.value?.toInt())
        val r = result.valute.get("USD")?.value?.toInt()
        val k = ((result.valute.get("USD")?.value!! - (result.valute.get("USD") as Rate).value.toInt()) * 100).toInt()

        if (r == null || k == null)
            return false

        var ruleBasedNumberFormat = com.ibm.icu.text.RuleBasedNumberFormat(java.util.Locale("ru"), RuleBasedNumberFormat.SPELLOUT)

        var text: String = "Курс доллара " + ruleBasedNumberFormat.format(r) + " " + rpetrov.senyavkaspeakingalarmclock.providers.Utils.getCorrectWordForDigit(r, " рубль", " рубля", " рублей", " рублей")
        text += ", " + ruleBasedNumberFormat.format(k.toLong()) + " " + rpetrov.senyavkaspeakingalarmclock.providers.Utils.getCorrectWordForDigit(k.toInt(),
                " копейка", " копейки", " копеек", " копеек")

        preparedText = text
        return result != null
    }

    override fun getText(): String {
        return preparedText
    }

    override fun getConfigLayout(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPermissions(): Array<String> = arrayOf()

    override fun getName() = "Курс доллара"

    override fun getDescription() = ""

    override fun isConfigurable() = false
}