package rpetrov.senyavkaspeakingalarmclock.providers.text.weather

/**
 * Created by Roman Petrov
 */

data class Result(val weather: List<Weather>, val main: Main, val wind: Wind?)
