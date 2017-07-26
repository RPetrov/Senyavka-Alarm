package ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.preference.EditTextPreference
import android.preference.PreferenceManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import rpetrov.senyavkaspeakingalarmclock.Application
import rpetrov.senyavkaspeakingalarmclock.database.dto.City


/**
 * Created by Roman Petrov
 */
class AutoCompletePreference(context: Context, attrs: AttributeSet) : EditTextPreference(context, attrs) {


    private val mEditText: AutoCompleteTextView = AutoCompleteTextView(context, attrs)
    private var currentCity: City? = null
    private var cities: List<City> = ArrayList()


    init {
        mEditText.threshold = 3
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, ArrayList<City>())


        Thread(Runnable {
            cities = Application.instance().citiesDao.queryForAll()
            Handler(Looper.getMainLooper()).post { adapter.addAll(cities) }
        }).start()
        mEditText.setAdapter(adapter)
    }

    override fun onBindDialogView(view: View) {
        val editText = mEditText
        editText.setText(text)
        editText.setSelection(text.length)

        val oldParent = editText.parent
        if (oldParent !== view) {
            if (oldParent != null) {
                (oldParent as ViewGroup).removeView(editText)
            }
            onAddEditTextToDialogView(view, editText)
        }

        mEditText.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentCity = parent!!.getItemAtPosition(position) as City
            }
        }


    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val value = mEditText.text.toString()
            if (callChangeListener(value)) {
                text = value
            }

            val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)


            sp.edit().putString("weather_city", text).apply()
            if (currentCity.toString() == text) {
                sp.edit().putFloat("weather_lan", currentCity!!.lat).putFloat("weather_lon", currentCity!!.lon).apply()
            } else {
                sp.edit().putFloat("weather_lan", -1f).putFloat("weather_lon", -1f).apply()
            }


        }
    }
}