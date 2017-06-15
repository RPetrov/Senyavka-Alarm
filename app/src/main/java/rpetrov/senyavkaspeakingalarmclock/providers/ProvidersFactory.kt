package rpetrov.senyavkaspeakingalarmclock.providers

import android.content.Context
import rpetrov.senyavkaspeakingalarmclock.providers.weather.WeatherProvider

/**
 * Created by Roman Petrov
 */

object ProvidersFactory {

    private var providers: List<IProvider>? = null

    fun getAll(context: Context) : List<IProvider> {
        if(providers == null){
            providers = arrayListOf(CurrentTimeProvider(), NowDateProvider(), WeatherProvider(context),
                    CurrencyProvider(), CalendarProvider(context))
        }
        return providers as List<IProvider>
    }
}