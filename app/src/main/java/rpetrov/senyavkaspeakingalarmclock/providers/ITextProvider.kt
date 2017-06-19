package rpetrov.senyavkaspeakingalarmclock.providers

/**
 * Created by Roman Petrov
 */


/**
 *
 */
interface ITextProvider : IProviderInfo, IProvider {
    fun prepare(): Boolean
    fun getText(): String
}