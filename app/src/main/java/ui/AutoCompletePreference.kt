package ui

import android.content.Context
import android.preference.EditTextPreference
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

        constructor(city: String, lan: Float, lon: Float, area: String) {
            this.city = city
            this.lan = lan
            this.lon = lon
            this.area = area
        }

        override fun toString(): String {
            return  city + ", " + area
        }
    }

    private var cities: ArrayList<City> = ArrayList()
    private val mEditText: AutoCompleteTextView = AutoCompleteTextView(context, attrs)

    init {


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

            mEditText.threshold = 0
            val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, cities)
            mEditText.setAdapter(adapter)

        } catch (e: IOException) {

        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                }
            }
        }

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
        }
    }
}