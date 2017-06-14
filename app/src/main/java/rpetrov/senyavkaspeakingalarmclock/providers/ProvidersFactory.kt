package rpetrov.senyavkaspeakingalarmclock.providers

import rpetrov.senyavkaspeakingalarmclock.providers.weather.WeatherProvider
import kotlin.reflect.KClass

/**
 * Created by Roman Petrov
 */
class ProvidersFactory{
    companion object {
        val providers = arrayListOf(CurrentTimeProvider::class, NowDateProvider::class, WeatherProvider::class,
                CurrencyProvider::class, CalendarProvider::class)
    }

    fun getAll() : ArrayList<KClass<out IProvider>> =  ProvidersFactory.providers
}