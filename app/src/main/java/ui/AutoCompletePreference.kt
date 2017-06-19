package ui

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.preference.EditTextPreference
import android.preference.PreferenceManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


/**
 * Created by Roman Petrov
 */
class AutoCompletePreference(context: Context, attrs: AttributeSet) : EditTextPreference(context, attrs) {

    class City {
        val city: String
        val lan: Float
        val lon: Float
        val area: String
        val printVersion: String

        constructor(city: String, lan: Float, lon: Float, area: String) {
            this.city = city
            this.lan = lan
            this.lon = lon
            this.area = area
            this.printVersion = city + ", " + area
        }

        override fun toString(): String {
            return printVersion
        }
    }

    private val mEditText: AutoCompleteTextView = AutoCompleteTextView(context, attrs)

    val cities: ArrayList<City> = ArrayList()

    init {


        val at: AsyncTask<Void, Void, ArrayList<City>> = object : AsyncTask<Void, Void, ArrayList<City>>() {
            override fun doInBackground(vararg params: Void?): ArrayList<City>? {


                var reader: BufferedReader? = null
                try {
                    reader = BufferedReader(
                            InputStreamReader(getContext().assets.open("ru-list.csv"), "UTF-8"))

                    // do reading, usually loop until end of file reading
                    var mLine: String?

                    while (true) {
                        mLine = reader.readLine()
                        if (mLine == null) break

                        val splitted = mLine.split(";")
                        cities.add(City(splitted[2], splitted[3].toFloat(), splitted[4].toFloat(), splitted[1] + ", " + splitted[0]))

                    }

                    return cities


                } catch (e: IOException) {

                } finally {
                    if (reader != null) {
                        try {
                            reader.close()
                        } catch (e: IOException) {
                        }
                    }
                }

                return null
            }

            override fun onPostExecute(result: ArrayList<City>?) {
                mEditText.threshold = 0
                val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, result)
                mEditText.setAdapter(adapter)
            }
        }

        at.execute()

    }

    override fun onBindDialogView(view: View) {
        val editText = mEditText
        editText!!.setText(text)
        editText.setSelection(text.length)

        val oldParent = editText.parent
        if (oldParent !== view) {
            if (oldParent != null) {
                (oldParent as ViewGroup).removeView(editText)
            }
            onAddEditTextToDialogView(view, editText)
        }


    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val value = mEditText.text.toString()
            if (callChangeListener(value)) {
                text = value
            }

            val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

            val city = cities.firstOrNull() { city -> city.printVersion.equals(text) }
            sp.edit().putString("weather_city", text).apply()
            if (city != null) {
                sp.edit().putFloat("weather_lan", city.lan).putFloat("weather_lon", city.lon).apply()
            } else {
                sp.edit().putFloat("weather_lan", -1f).putFloat("weather_lon", -1f).apply()
            }


        }
    }
}