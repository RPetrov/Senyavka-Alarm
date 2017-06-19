package rpetrov.senyavkaspeakingalarmclock.providers.text

import android.content.SharedPreferences
import android.preference.PreferenceManager
import rpetrov.senyavkaspeakingalarmclock.providers.IProvider
import rpetrov.senyavkaspeakingalarmclock.providers.IProviderInfo


/**
 * Created by Roman Petrov
 */
abstract class BaseProvider : IProvider, IProviderInfo {

    val context: android.content.Context

    constructor(context: android.content.Context) {
        this.context = context
    }

    override fun isEnable(): Boolean {
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sp.getBoolean(getName(), false)
    }

    override fun enable(boolean: Boolean) {
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sp.edit().putBoolean(getName(), boolean).apply()
    }
}