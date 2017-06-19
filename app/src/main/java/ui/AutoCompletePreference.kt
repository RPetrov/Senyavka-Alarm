package ui

import android.content.Context
import android.content.SharedPreferences
import android.preference.EditTextPreference
import android.preference.PreferenceManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import rpetrov.senyavkaspeakingalarmclock.Cities


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



    init {
        mEditText.threshold = 0
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, Cities.Cities.cities)
        mEditText.setAdapter(adapter)
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

            val city = Cities.Cities.cities.firstOrNull { city -> city.printVersion.equals(text) }
            sp.edit().putString("weather_city", text).apply()
            if (city != null) {
                sp.edit().putFloat("weather_lan", city.lan).putFloat("weather_lon", city.lon).apply()
            } else {
                sp.edit().putFloat("weather_lan", -1f).putFloat("weather_lon", -1f).apply()
            }


        }
    }
}