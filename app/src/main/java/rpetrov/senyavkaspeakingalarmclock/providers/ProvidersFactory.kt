package rpetrov.senyavkaspeakingalarmclock.providers

import android.content.Context
import rpetrov.senyavkaspeakingalarmclock.providers.text.*
import rpetrov.senyavkaspeakingalarmclock.providers.text.weather.WeatherProvider

/**
 * Created by Roman Petrov
 */

object ProvidersFactory {

    private var textProviders: List<ITextProvider>? = null
    private var runnableProviders: List<IRunnableProvider>? = null

    fun getAll(context: Context) : List<IProviderInfo> {
        var providers: ArrayList<IProviderInfo> = ArrayList<IProviderInfo>()
        providers.addAll(getAllTextProvider(context))
        providers.addAll(getAllRunnableProvider(context))

        return providers
    }

    fun getAllTextProvider(context: Context) : List<ITextProvider> {
        if(textProviders == null){
            textProviders = arrayListOf(CurrentTimeProvider(context), NowDateProvider(context), WeatherProvider(context),
                    CurrencyProvider(context), CalendarProvider(context))
        }
        return textProviders as List<ITextProvider>
    }

    fun getAllRunnableProvider(context: Context) : List<IRunnableProvider> {
        if(runnableProviders == null){
            runnableProviders = arrayListOf(MusicProvider(context))
        }
        return runnableProviders as List<IRunnableProvider>
    }
}